package cn.cy.springbatch.config.p05;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 决策器的使用
 */
@Configuration
@EnableBatchProcessing
public class DeciderDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job deciderDemoJob(){
        return jobBuilderFactory.get("deciderDemoJob")
                .start(DeciderStep1())
                .next(myDecider())
                .from(myDecider()).on("even").to(DeciderStep2())
                .from(myDecider()).on("odd").to(DeciderStep3())
                .from(DeciderStep3()).on("*").to(myDecider())
                .end()
                .build();
    }

    //注入决策器
    @Bean
    public JobExecutionDecider myDecider(){
        return new MyDecider();
    }

    @Bean
    public Step DeciderStep1() {
        return stepBuilderFactory.get("DeciderStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("DeciderStep1");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step DeciderStep2(){
        return stepBuilderFactory.get("DeciderStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("DeciderStep2");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step DeciderStep3(){
        return stepBuilderFactory.get("DeciderStep3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("DeciderStep3");
                    return RepeatStatus.FINISHED;
                }).build();
    }





}
