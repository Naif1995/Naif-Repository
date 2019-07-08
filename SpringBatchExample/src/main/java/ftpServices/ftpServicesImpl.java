package ftpServices;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service("FtpService")
public class ftpServicesImpl implements ftpServices {
	


    
    
	@Override
	public FTPClient getFilesName() throws Exception {
		
		ArrayList<String> filesName = new ArrayList<String>(); // Create an ArrayList object

		
		String server = "192.168.100.87";
        int port = 21;
        String user = "Naif";
        String pass = "0540575390";
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
               // return ftpClient;
               // throw new Exception();
            }
            boolean success = ftpClient.login(user, pass);
            showServerReply(ftpClient);
            if (!success) {
                System.out.println("Could not login to the server");
                //return ftpClient;
               // throw new Exception();
            } else {
                System.out.println("LOGGED IN SERVER");


             // Get the files stored on FTP Server and store them into an array of FTPFiles
                FTPFile[] files = ftpClient.listFiles();
                 
                for (FTPFile ftpFile : files) {
                    // Check the file type and print result
                 
                    if (ftpFile.getType() == FTPFile.FILE_TYPE) {
                 
                  System.out.println("File: " + ftpFile.getName() );
                    }
                }
               
            }
        } catch (IOException ex) {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
        }
		return ftpClient;
        
    }
	
        
        private static void showServerReply(FTPClient ftpClient) {
            String[] replies = ftpClient.getReplyStrings();
            if (replies != null && replies.length > 0) {
                for (String aReply : replies) {
                    System.out.println("SERVER: " + aReply);
                }
            }
        }

        /**
         * Upload a whole directory (including its nested sub directories and files)
         * to a FTP server.
         *
         * @param ftpClient
         *            an instance of org.apache.commons.net.ftp.FTPClient class.
         * @param remoteDirPath
         *            Path of the destination directory on the server.
         * @param localParentDir
         *            Path of the local directory being uploaded.
         * @param remoteParentDir
         *            Path of the parent directory of the current directory on the
         *            server (used by recursive calls).
         * @throws IOException
         *             if any network or IO error occurred.
         */
        public void uploadDirectory(FTPClient ftpClient,
                String remoteDirPath, String localParentDir, String remoteParentDir)
                throws IOException {
         
            System.out.println("LISTING directory: " + localParentDir);
         
            File localDir = new File(localParentDir);
            File[] subFiles = localDir.listFiles();

            if (subFiles != null && subFiles.length > 0) {
                for (File item : subFiles) {
                    String remoteFilePath = remoteDirPath + "/" + remoteParentDir
                            + "/" + item.getName();
                    if (remoteParentDir.equals("")) {
                        remoteFilePath = remoteDirPath + "/" + item.getName();
                    }
         
         
                    if (item.isFile()) {
                        // upload the file
                        String localFilePath = item.getAbsolutePath();
                        System.out.println("About to upload the file: " + localFilePath);
                        boolean uploaded = uploadSingleFile(ftpClient,
                                localFilePath, remoteFilePath);
                        if (uploaded) {
                            System.out.println("UPLOADED a file to: "
                                    + remoteFilePath);
                        } else {
                            System.out.println("COULD NOT upload the file: "
                                    + localFilePath);
                        }
                    } else {
                        // create directory on the server
                        boolean created = ftpClient.makeDirectory(remoteFilePath);
                        if (created) {
                            System.out.println("CREATED the directory: "
                                    + remoteFilePath);
                        } else {
                            System.out.println("COULD NOT create the directory: "
                                    + remoteFilePath);
                        }
         
                        // upload the sub directory
                        String parent = remoteParentDir + "/" + item.getName();
                        if (remoteParentDir.equals("")) {
                            parent = item.getName();
                        }
         
                        localParentDir = item.getAbsolutePath();
                        uploadDirectory(ftpClient, remoteDirPath, localParentDir,
                                parent);
                    }
                }
            }
        }
        
        
        /**
         * Upload a single file to the FTP server.
         *
         * @param ftpClient
         *            an instance of org.apache.commons.net.ftp.FTPClient class.
         * @param localFilePath
         *            Path of the file on local computer
         * @param remoteFilePath
         *            Path of the file on remote the server
         * @return true if the file was uploaded successfully, false otherwise
         * @throws IOException
         *             if any network or IO error occurred.
         */
        public static boolean uploadSingleFile(FTPClient ftpClient,
                String localFilePath, String remoteFilePath) throws IOException {
            File localFile = new File(localFilePath);
         
            InputStream inputStream = new FileInputStream(localFile);
            try {
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                return ftpClient.storeFile(remoteFilePath, inputStream);
            } finally {
                inputStream.close();
            }
        }
        
        /**
         * Download a single file from the FTP server
         * @param ftpClient an instance of org.apache.commons.net.ftp.FTPClient class.
         * @param remoteFilePath path of the file on the server
         * @param savePath path of directory where the file will be stored
         * @return true if the file was downloaded successfully, false otherwise
         * @throws IOException if any network or IO error occurred.
         */
        public static boolean downloadSingleFile(FTPClient ftpClient,
                String remoteFilePath, String savePath) throws IOException {
            File downloadFile = new File(savePath);
            
            InputStream input = null;
             
            File parentDir = downloadFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdir();
            }
                 
            OutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(downloadFile));
            try {
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                
                
            //  return ftpClient.retrieveFile(remoteFilePath, outputStream);
                input  =ftpClient.retrieveFileStream(remoteFilePath);
                if(input != null) {
                	byte[] bytes = IOUtils.toByteArray(input);
                	// encode string using Base64 encoder
            	//	String encoded = DatatypeConverter.printBase64Binary(bytes);
            	//	System.out.println("Encoded String: " + encoded);

                	



                return true ; 
                
                
                }
                return false ; 
              
            } catch (IOException ex) {
                throw ex;
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
        
        
        /**
         * Download a whole directory from a FTP server.
         * @param ftpClient an instance of org.apache.commons.net.ftp.FTPClient class.
         * @param parentDir Path of the parent directory of the current directory being
         * downloaded.
         * @param currentDir Path of the current directory being downloaded.
         * @param saveDir path of directory where the whole remote directory will be
         * downloaded and saved.
         * @throws IOException if any network or IO error occurred.
         */
        public  void downloadDirectory(FTPClient ftpClient, String parentDir,
                String currentDir, String saveDir) throws IOException {
            String dirToList = parentDir;
            if (!currentDir.equals("")) {
                dirToList += "/" + currentDir;
            }
         
            FTPFile[] subFiles = ftpClient.listFiles(dirToList);
         
            if (subFiles != null && subFiles.length > 0) {
                for (FTPFile aFile : subFiles) {
                    String currentFileName = aFile.getName();
                    if (currentFileName.equals(".") || currentFileName.equals("..")||currentFileName.startsWith(".")) {
                        // skip parent directory and the directory itself
                        continue;
                    }
                    String filePath = parentDir + "/" + currentDir + "/"
                            + currentFileName;
                    if (currentDir.equals("")) {
                        filePath = parentDir + "/" + currentFileName;
                    }
         
                    String newDirPath = saveDir + parentDir + File.separator
                            + currentDir + File.separator + currentFileName;
                    if (currentDir.equals("")) {
                        newDirPath = saveDir + parentDir + File.separator
                                  + currentFileName;
                    }
         
                    if (aFile.isDirectory()) {
                        // create the directory in saveDir
                        File newDir = new File(newDirPath);
                        boolean created = newDir.mkdirs();
                        if (created) {
                            System.out.println("CREATED the directory: " + newDirPath);
                        } else {
                            System.out.println("COULD NOT create the directory: " + newDirPath);
                        }
         
                        // download the sub directory
                        downloadDirectory(ftpClient, dirToList, currentFileName,
                                saveDir);
                    } else {
                        // download the file
                        boolean success = downloadSingleFile(ftpClient, filePath,
                                newDirPath);
                        if (success) {
                            System.out.println("DOWNLOADED the file: " + filePath);
                        } else {
                            System.out.println("COULD NOT download the file: "
                                    + filePath);
                        }
                    }
                }
            }
        }



	
        
	}
	
	
	
	


