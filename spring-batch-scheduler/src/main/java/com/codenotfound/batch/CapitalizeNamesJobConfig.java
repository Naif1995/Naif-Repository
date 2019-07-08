package com.codenotfound.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import com.codenotfound.model.Person;

@Configuration
@EnableBatchProcessing
public class CapitalizeNamesJobConfig {

	 	@Autowired
	    private JobBuilderFactory jobs;
	 
	    @Autowired
	    private StepBuilderFactory steps;
  
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
  
}
