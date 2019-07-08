package com.codenotfound.batch;

import java.sql.Driver;
import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class CapitalizeNamesJobScheduler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(CapitalizeNamesJobScheduler.class);

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private Job capitalizeNamesJob;

  @Autowired
  public DataSource dataSource;
  


  @Scheduled(cron = "0/10 * * * * ?")
  public void runBatchJob()
      throws JobExecutionAlreadyRunningException,
      JobRestartException, JobInstanceAlreadyCompleteException,
      JobParametersInvalidException {
    LOGGER.info("start runBatchJob");


      jobLauncher.run(capitalizeNamesJob, new JobParametersBuilder()
          .addDate("date", new Date()).toJobParameters());
    
  }

 
}
