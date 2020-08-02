package cn.cy.springbatch.config.p01;
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
 * 快速开始
 */
@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    // 注入创建Job的对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    //注入创建Step的对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    //创建Job
    @Bean
    public Job HelloWorldJob(){
        return  jobBuilderFactory.get("HelloWorldJob")
                .start(step1())
                .build();
    }

    //创建Step
    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("hello world!");
                        //指定执行完的状态值 只有正常执行结束 才会执行后续的step
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }
}
