package cn.cy.springbatch.config.p25;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class JobLauncherDemo implements StepExecutionListener {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    //用来存储Job运行所需的参数
    private Map<String, JobParameter> jobParameterMap;

    @Bean
    public Job launcherJob2(){
        return jobBuilderFactory.get("launcherJob2")
                .start(launcherStep())
                .build();
    }

    @Bean
    public Step launcherStep(){
        return stepBuilderFactory.get("launcherStep")
                .listener(this)  //开启监听
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("launcherStep 执行... 执行参数时： "+jobParameterMap.get("msg").getValue());
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        jobParameterMap = stepExecution.getJobParameters().getParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
