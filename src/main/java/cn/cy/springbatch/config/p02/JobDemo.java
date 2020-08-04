package cn.cy.springbatch.config.p02;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 限定Step的顺序 以及执行条件
 */
@Configuration
@EnableBatchProcessing
public class JobDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobDemoJob(){
        return jobBuilderFactory.get("jobDemoJob")
                //指定step的顺序 按序执行
                /*.start(step01())
                .next(step02())
                .next(step03())
                .build();*/

                //指定step的顺序 以及 执行下一个Step的条件
                .start(step01())
                .on("COMPLETED").to(step02())
                .from(step02()).on("COMPLETED").to(step03())
                .from(step03()).end()
                //该job永远不会重启，只会执行一次
//                .preventRestart()
                .build();
    }


    @Bean
    public Step step01() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step1.....");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step step02() {
        return stepBuilderFactory.get("step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step2.....");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step step03() {
        return stepBuilderFactory.get("step3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step3.....");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

}
