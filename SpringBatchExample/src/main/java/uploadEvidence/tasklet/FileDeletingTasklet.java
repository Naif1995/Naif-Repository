package uploadEvidence.tasklet;


import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.InputStream;

import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import ftpServices.ftpServices;


@ComponentScan("ftpServices")
public class FileDeletingTasklet implements Tasklet, InitializingBean {

	private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private Resource sql;

	
	
    @Autowired
    @Qualifier("FtpService")        
    private ftpServices service ;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		try {
            InputStream input = null;

		
			 String remoteDirPath = "/upload";
	         String localDirPath = "/Users/naif/Desktop/DownloadFTP";
			 FTPClient ftpClient	= service.getFilesName();
		   //service.uploadDirectory(ftpClient,remoteDirPath,localDirPath,"");
			 
			// 192.168.100.87
			 
			 ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
             
             
	           //  return ftpClient.retrieveFile(remoteFilePath, outputStream);
//	                input  =ftpClient.retrieveFileStream("/upload/zipfile/Projec Rest/InjuryID/pom.xml");
//	                if(input != null) {
//	                	byte[] bytes = IOUtils.toByteArray(input);
//	                	
//	                	String encoded = DatatypeConverter.printBase64Binary(bytes);
//	                	System.out.println("Encoded String: " + encoded);
//	                }
//			 
			 service.downloadDirectory(ftpClient, remoteDirPath, "", localDirPath);
			 // log out and disconnect from the server
            ftpClient.logout();
            ftpClient.disconnect();
 
            System.out.println("Disconnected");
		
		System.out.print("Works Fine" );
		
		}catch(Exception e) {
			
			e.printStackTrace();
			
		}
	
		
		
		return RepeatStatus.FINISHED;

	}

	public void insert(){
		  
        String sql = "INSERT INTO CUSTOMERS " +
            "(ID, NAME, AGE) VALUES (CUSTOMERS.nextval, 'NAIF', 25)";
  
       // jdbcTemplate = new JdbcTemplate(dataSource);
  
        jdbcTemplate.update(sql);
    }
	



	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

//	public void setDataSource(DataSource dataSource) {
//		this.dataSource = dataSource;
//	}

	public Resource getSql() {
		return sql;
	}

	public void setSql(Resource sql) {
		this.sql = sql;
	}
	




}