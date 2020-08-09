package cn.cy.springbatch.config.p23;

import cn.cy.springbatch.config.p22.CustomRetryException;
import cn.cy.springbatch.config.p22.RetryItemProcessor;
import cn.cy.springbatch.config.p22.RetryItemWriter;
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
public class SkipDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Resource
    private RetryItemWriter retryItemWriter;
    @Resource
    private RetryItemProcessor retryItemProcessor;

    @Bean
    public Job skipJobDemo(){
        return jobBuilderFactory.get("skipJobDemo")
                .start(skipStep())
                .build();
    }

    @Bean
    public Step skipStep(){
        return stepBuilderFactory.get("skipStep")
                .<String,String>chunk(10)
                .reader(skipReader())
                .processor(retryItemProcessor)
                .writer(retryItemWriter)
                .faultTolerant() //容错
                .skip(CustomRetryException.class)//reader、writer、proccessor中发生该异常时跳过异常
                .skipLimit(5)//最多跳过异常的次数
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<String> skipReader(){
        List<String> items  = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            items.add(String.valueOf(i));
        }
        return new ListItemReader<String>(items);
    }

}
