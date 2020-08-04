package cn.cy.springbatch.config.p07;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * 创建Job的监听器
 */
public class MyJobListener implements JobExecutionListener {


    /**
     * Job执行之前
     * @param jobExecution
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(jobExecution.getJobInstance().getJobName()+"准备执行");
    }

    /**
     * Job执行之后，无论Job执行成功与否
     * @param jobExecution
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println(jobExecution.getJobInstance().getJobName()+"执行完毕");
    }

}
