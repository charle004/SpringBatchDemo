package cn.cy.springbatch.config.p08;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Job的参数 Job实际执行的是Step Job的参数其实就是Step的参数
 *
 * 使用Step的监听器实现参数的传递
 */
@Configuration
@EnableBatchProcessing
public class ParameterDemo implements JobExecutionListener {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    // 储存job的参数
    private Map<String, JobParameter> parameterMap;

    @Bean
    public Job parameterJob(){
        return jobBuilderFactory.get("parameterJob")
                .start(parameterStep())
                .build();
    }

    @Bean
    public Step parameterStep(){
        return stepBuilderFactory.get("parameterStep")
                .listener(this)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("ParamStep...");
                        if (parameterMap!=null && parameterMap.containsKey("info")) {
                            System.out.println("接收的参数："+parameterMap.get("info"));
                        }
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        parameterMap = jobExecution.getJobParameters().getParameters();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }
}
