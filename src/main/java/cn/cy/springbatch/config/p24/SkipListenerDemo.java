package cn.cy.springbatch.config.p24;

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
public class SkipListenerDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Resource
    private RetryItemWriter retryItemWriter;
    @Resource
    private RetryItemProcessor retryItemProcessor;
    @Resource
    private MySkipListener mySkipListener;

    @Bean
    public Job skipListenerJobDemo(){
        return jobBuilderFactory.get("skipListenerJobDemo")
                .start(skipListenStep())
                .build();
    }

    @Bean
    public Step skipListenStep(){
        return stepBuilderFactory.get("skipListenStep")
                .<String,String>chunk(10)
                .reader(skipListenReader())
                .processor(retryItemProcessor)
                .writer(retryItemWriter)
                .faultTolerant() //容错
                .skip(CustomRetryException.class)
                .skipLimit(5)
                .listener(mySkipListener)//启动SkipListener记录SKip的Item
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<String> skipListenReader(){
        List<String> items  = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            items.add(String.valueOf(i));
        }
        return new ListItemReader<String>(items);
    }

}
