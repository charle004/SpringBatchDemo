package cn.cy.springbatch.config.p25;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
public class MyController {

    //注入一个默认的JobLauncher
    @Autowired
    private JobLauncher jobLauncher;
    @Resource
    private Job launcherJob2;

    @GetMapping("/index")
    public String toPage(){
        return "/index";
    }

    @RequestMapping("/job/test/{msg}")
    @ResponseBody
    public String runJob(@PathVariable String msg){
        //前端传参封装进 Job的参数中
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("msg", msg)
                .toJobParameters();

        try {

            //使用jobLauncher执行任务 并将参数传进Job中
            jobLauncher.run(launcherJob2,jobParameters);

        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }

        return "job success";
    }

}
