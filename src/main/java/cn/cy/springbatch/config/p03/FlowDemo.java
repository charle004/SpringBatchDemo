package cn.cy.springbatch.config.p03;


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

/**
 * Flow 是Step对象的组合
 */
@Configuration
@EnableBatchProcessing
public class FlowDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flowDemoJob(){
        return jobBuilderFactory.get("FlowDemoJob")
                .start(flowDemoFlow())
                .next(flowDemostep3())
                .end()
                .build();
    }

    //创建Flow对象
    @Bean
    public Flow flowDemoFlow(){
        //指定Flow对象包含哪些Step
        return new FlowBuilder<Flow>("flowDemoFlow")
                .start(flowDemostep1())
                .next(flowDemostep2())
                .build();

    }

    @Bean
    public Step flowDemostep1(){
        //Lamba简化写法
        return stepBuilderFactory.get("flowDemoStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("flowDemoStep1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


    @Bean
    public Step flowDemostep2(){
        //Lamba简化写法
        return stepBuilderFactory.get("flowDemoStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("flowDemoStep2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step flowDemostep3(){
        //Lamba简化写法
        return stepBuilderFactory.get("flowDemoStep3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("flowDemoStep3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


}
