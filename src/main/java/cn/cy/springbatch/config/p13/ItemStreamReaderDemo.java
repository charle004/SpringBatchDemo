package cn.cy.springbatch.config.p13;


import cn.cy.springbatch.domain.PersonBean;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class ItemStreamReaderDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Resource
    private RestartReader restartReader;
    @Resource
    private ItemStreamReaderWriter writer;

    @Bean
    public Job ItemStreamReaderJob(){
        return jobBuilderFactory.get("ItemStreamReaderJob2")
                .start(ItemStreamReaderStep())
                .build();
    }

    @Bean
    public Step ItemStreamReaderStep(){
        return stepBuilderFactory.get("ItemStreamReaderStep")
                .<PersonBean,PersonBean>chunk(10)
                .reader(restartReader)
                .writer(writer)
                .build();
    }

}
