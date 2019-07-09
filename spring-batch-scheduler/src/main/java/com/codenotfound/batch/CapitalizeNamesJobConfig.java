package com.codenotfound.batch;


import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;



@Configuration
@EnableBatchProcessing
public class CapitalizeNamesJobConfig {

	 	@Autowired
	    private JobBuilderFactory jobs;
	 
	    @Autowired
	    private StepBuilderFactory steps;
	    
		JdbcTemplate jdbcTemplate ;

  
  @Bean
  public Step stepOne(){
      return steps.get("stepOne")
              .tasklet(new UploadFiles())
              .build();
  }
  
  
  @Bean
  public Job demoJob(){
      return jobs.get("demoJob")
              .incrementer(new RunIdIncrementer())
              .start(stepOne())
              .build();
  }
  
  
  @Bean
  public Tasklet tasklet(){
		 jdbcTemplate = new JdbcTemplate(dataSource()) ;
 ;

	  UploadFiles uploadFiles = new UploadFiles();
	  uploadFiles.setJdbcTemplate(jdbcTemplate);
      return uploadFiles;
  }
  
  
  
  @Bean
  public DataSource dataSource() {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName("org.h2.Driver");
      dataSource.setUrl("jdbc:h2:~/test");
      dataSource.setUsername("sa");
      dataSource.setPassword("");
      return dataSource;
  }




}
