package cn.cy.springbatch.config.p04;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * Step / Flow 对象的并发执行
 */
@Configuration
@EnableBatchProcessing
public class SplitDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job SplitDemoJob(){
        return jobBuilderFactory.get("splitDemoJob")
                .start(SplitDemoFlow1())
                //异步执行对象
                .split(new SimpleAsyncTaskExecutor())
                .add(SplitDemoFlow2())
                .end()
                .build();
    }

    @Bean
    public Flow SplitDemoFlow1(){
        return new FlowBuilder<Flow>("SplitDemoFlow1")
                .start(splitDemoStep1())
                .build();
    }

    @Bean
    public Flow SplitDemoFlow2(){
        return new FlowBuilder<Flow>("SplitDemoFlow2")
                .start(splitDemoStep2())
                .next(splitDemoStep3())
                .build();
    }

    @Bean
    public Step splitDemoStep1(){
        return stepBuilderFactory.get("splitDemoStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("splitDemoStep1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step splitDemoStep2(){
        return stepBuilderFactory.get("splitDemoStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("splitDemoStep2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step splitDemoStep3(){
        return stepBuilderFactory.get("splitDemoStep3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("splitDemoStep3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
