package cn.cy.springbatch.config.p09;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ItemReaderDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job itemReaderJob(){
        return jobBuilderFactory.get("itemReaderJob")
                .start(itemReaderStep())
                .build();
    }

    @Bean
    public Step itemReaderStep(){
        return stepBuilderFactory.get("itemReaderStep")
                .chunk(2)
                //指定一个ItemReader对象
                .reader(itemReaderDemoRead())
                //输出数据
                .writer(list->{
                    list.forEach(System.out::println);
                }).build();
    }

    //向自定义的ItemReader对象中传递数据源参数
    public ItemReader<String> itemReaderDemoRead(){
        List<String> list = Arrays.asList("cat","dog","pig");
        return new MyReader(list);
    }

}
