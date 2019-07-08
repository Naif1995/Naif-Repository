package uploadEvidence.tasklet;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
 
public class MyScheduler {
 
    @Autowired
    private JobLauncher launcher;
     
    @Autowired
    private Job job;
     
    private JobExecution execution;
     
    public void run(){      
        try {
        	JobParameters jobParameters = new JobParametersBuilder()
	                .addDate("date", new Date())
	                .addLong("time",System.currentTimeMillis()).toJobParameters();
            execution = launcher.run(job, jobParameters);
            System.out.println("Execution status: "+ execution.getStatus());
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {           
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {           
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {         
            e.printStackTrace();
        }
    }
}