package cn.cy.springbatch.config.p21;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Configuration
public class ErrorDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job ErrorDemoJob(){
        return jobBuilderFactory.get("ErrorDemoJob")
                .start(errorStep1())
                .next(errorStep2())
                .build();
    }

    @Bean
    public Step errorStep1(){
        return stepBuilderFactory.get("errorStep1")
                .tasklet(errorHandling())
                .build();
    }
    @Bean
    public Step errorStep2(){
        return stepBuilderFactory.get("errorStep2")
                .tasklet(errorHandling())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet errorHandling(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                //获取当前Step执行的上下文
                Map<String, Object> stepExecutionContext = chunkContext.getStepContext().getStepExecutionContext();

                if(stepExecutionContext.containsKey("qianfeng")){
                    System.out.println("Second Run will success");
                    return RepeatStatus.FINISHED;
                }

                else{
                    System.out.println("First Run will fail");
                    chunkContext.getStepContext().getStepExecution().getExecutionContext().put("qianfeng",true);
                    throw new RuntimeException("error...");
                }
            }
        };
    }





















}
