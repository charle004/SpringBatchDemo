package cn.cy.springbatch.config.p22;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RetryDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Resource
    private RetryItemWriter retryItemWriter;
    @Resource
    private RetryItemProcessor retryItemProcessor;

    @Bean
    public Job retryJobDemo(){
        return jobBuilderFactory.get("retryJobDemo")
                .start(retryStep())
                .build();
    }

    @Bean
    public Step retryStep(){
        return stepBuilderFactory.get("retryStep")
                .<String,String>chunk(10)
                .reader(retryReader())
                .processor(retryItemProcessor)
                .writer(retryItemWriter)
                .faultTolerant() //容错
                .retry(CustomRetryException.class)//当发生该异常时，重试该任务
                .retryLimit(5)//重试次数限制
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<String> retryReader(){
        List<String> items  = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            items.add(String.valueOf(i));
        }
        return new ListItemReader<String>(items);
    }

}
