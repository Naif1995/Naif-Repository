package com.codenotfound.batch;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

public class UploadFiles implements Tasklet {
	
	DataSource dataSource;
	
	JdbcTemplate jdbcTemplate ;

	 private static final Logger LOGGER = LoggerFactory.getLogger(UploadFiles.class);

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception
    {
    	
    	 
        LOGGER.trace("doStuff needed more information - {}");
        LOGGER.debug("doStuff needed to debug - {}");
        LOGGER.info("doStuff took input - {}");
        LOGGER.warn("doStuff needed to warn - {}");
        LOGGER.error("doStuff encountered an error with value - {}");
    	
        System.out.println("MyTaskTwo start..");
 
        // ... your code
         
        System.out.println("MyTaskTwo done..");
        return RepeatStatus.FINISHED;
    }



	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		
		if (jdbcTemplate != null) {
			
	        System.out.println("MyTaskTwo Tow..");

		}
		
		this.jdbcTemplate = jdbcTemplate;
	}
    
    
    
    
}
