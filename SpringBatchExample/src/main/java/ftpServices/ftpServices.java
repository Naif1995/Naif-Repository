package ftpServices;

import java.io.IOException;
import java.util.Optional;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;


public  interface ftpServices {
	
	
	public FTPClient getFilesName() throws Exception;
	
	public void uploadDirectory(FTPClient ftpClient,String remoteDirPath, String localParentDir, String remoteParentDir)throws IOException;
	
	public  void downloadDirectory(FTPClient ftpClient, String parentDir,String currentDir, String saveDir) throws IOException;

	



}
