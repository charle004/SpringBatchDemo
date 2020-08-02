package cn.cy.springbatch.config.p07;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;


/**
 * 测试 Job监听器
 */
@Configuration
@EnableBatchProcessing
public class ListenerDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job listnerJob(){
        return jobBuilderFactory.get("listnerJob")
                .start(listnerStep())
                //指定Job的监听器
                .listener(new MyJobListener())
                .build();
    }

    @Bean
    public Step listnerStep(){
        return stepBuilderFactory.get("listnerStep")
            //每次读取两个数据作输出处理 并指定读取和输出的数据类型
            .<String,String>chunk(2)
            //容错用
            .faultTolerant()
            //指定chunk监听器
            .listener(new MyChunkListener())
            .reader(read())
            .writer(write())
            .build();
    }

    /**
     * 使用ItemReader的实现类 创建ItemReader对象
     * @return temReader对象
     */
    @Bean
    public ItemReader<String> read(){
        List<String> list = Arrays.asList("aa", "bb", "cc","dd");
        return new ListItemReader<>(list);
    }

    /**
     * 创建 ItemWriter对象 实现接口方法
     * @return ItemWriter对象
     */
    @Bean
    public ItemWriter<String> write(){
        return new ItemWriter<String>() {
            //ItemReader读取到的数据 以集合的形式传入 ItemWriter
            @Override
            public void write(List<? extends String> items) throws Exception {
                items.forEach(System.out::println);
            }
        };
    }

}
