package cn.cy.springbatch.config.p06;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Job的嵌套
 */
@Configuration
@EnableBatchProcessing
public class NestedDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    //注入其他的Job对象
    @Autowired
    private Job childJob1;
    //注入其他的Job对象
    @Autowired
    private Job childJob2;

    //注入启动Job的JobLauncher
    @Autowired
    private JobLauncher jobLauncher;


    //创建父Job
    @Bean
    public Job parentJob(JobRepository registry, PlatformTransactionManager transactionManager){
        return jobBuilderFactory.get("parentJob")
                .start(parentChildJob1(registry,transactionManager))
                .next(parentChildJob2(registry,transactionManager))
                .build();
    }


    /**
     * 创建JOb类型的Step对象
     * @param registry 对此Job进行持久化的对象
     * @param transactionManager 对此Job持久化的事务管理器
     * @return
     */
    private Step parentChildJob1(JobRepository registry, PlatformTransactionManager transactionManager){
        return new JobStepBuilder(new StepBuilder("parentChildJob1"))
                .job(childJob1)
                //使用启动父Job的对象启动子Job
                .launcher(jobLauncher)
                //指定持久化对象
                .repository(registry)
                //指定持久化事务管理器
                .transactionManager(transactionManager)
                .build();
    }


    /**
     * 创建JOb类型的Step对象
     * @param registry 对此Job进行持久化的对象
     * @param transactionManager 对此Job持久化的事务管理器
     * @return
     */
    private Step parentChildJob2(JobRepository registry,PlatformTransactionManager transactionManager){
        return new JobStepBuilder(new StepBuilder("parentChildJob2"))
                .job(childJob2)
                //使用启动父Job的对象启动子Job
                .launcher(jobLauncher)
                //指定持久化对象
                .repository(registry)
                //执行持久化事务管理器
                .transactionManager(transactionManager)
                .build();
    }

}
