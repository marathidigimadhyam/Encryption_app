package pureclass;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Publish_App
{
  private static String OAUTH_SCOPE = "https://www.googleapis.com/auth/androidpublisher";
  private static final String APP_NAME = "EduPhysics Dictionary";
  private static final String ALGO_RANDOM_NUM_GENERATOR = "SHA1PRNG";
  private static final String ALGO_SECRET_KEY_GENERATOR = "AES";
  private static final int IV_LENGTH = 16;
  
  public Publish_App() { System.out.println("Initialize publish app "); }
  
  public void deleteDir(File file) {
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            if (! Files.isSymbolicLink(f.toPath())) {
	                deleteDir(f);
	            }
	        }
	    }
	    file.delete();
	}
  
  public static void usingBufferedWritter(String filePath,String textToAppend)
  {
      try {
          
    	  String[] splitPath = filePath.split("uploads/");
    	  String folderPath = splitPath[0]+"uploads/filelog.txt";
    	  File uploadDir = new File(folderPath);
        
          if (!uploadDir.exists()) { 
          	uploadDir.createNewFile();
          }
          BufferedWriter writer = new BufferedWriter(
                                      new FileWriter(folderPath, true)  //Set true for append mode
                                  ); 
          writer.newLine();   //Add new line
          writer.write(textToAppend);
          writer.close();  
      }catch (Exception e) {
		// TODO: handle exception
	}
  }
  
  public String saveUploadAppDetails(String inputFilePath, String destinationFolderPath)
  {
	  
	String returnpath = destinationFolderPath;
    try
    {
    	int index = inputFilePath.lastIndexOf("/");
    	String fileName = inputFilePath.substring(index + 1);
      //String[] fileNameArr = inputFilePath.split("");
      //String fileName = fileNameArr[(fileNameArr.length - 1)];
      //fileName = fileName.replaceFirst("[.][^.]+$", "");
      
      System.out.println("inputFilePath --" + inputFilePath);
      System.out.println("destinationFolderPath --" + destinationFolderPath);
      byte[] buf = new byte[' '];
      Cipher c = Cipher.getInstance("AES/CTR/NoPadding");
      byte[] iv = new byte[16];
      IvParameterSpec ivspec = new IvParameterSpec(iv);
      
      c.init(1, new SecretKeySpec("cRfUjXnZr4u7x!A%D*G-$@#EbCSaYp3s".getBytes(), "AES"), ivspec);
      InputStream is = new FileInputStream(inputFilePath);
      String filePath = destinationFolderPath + "/" + fileName;
      OutputStream os = new CipherOutputStream(new FileOutputStream(filePath), c);
      for (;;) {
        int n = is.read(buf);
        if (n == -1) break;
        os.write(buf, 0, n);
      }
      os.close();is.close();
      returnpath = filePath;
      usingBufferedWritter(returnpath,fileName);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return returnpath;
  }
  
  public List<File> listFilesForFolder(File folder)
  {
    List<File> fileListArr = new ArrayList();
    try {
      for (File fileEntry : folder.listFiles()) {
        if (fileEntry.isDirectory()) {
          listFilesForFolder(fileEntry);
        } else {
          System.out.println(fileEntry.getName());
          String mimeType = Files.probeContentType(fileEntry.toPath());
          System.out.println("mimeType - " + mimeType);
          if (mimeType.contains("video")) {
            fileListArr.add(fileEntry);
          }
        }
      }
    }
    catch (Exception localException) {}
    
    return fileListArr;
  }
  
  public String encryptFolderData(String pickFilePath, String folderName)
  {
    String message = "Completed";
    try
    {
      File parent = new File(pickFilePath);
      String destFolderPath = parent.getAbsolutePath() + "/" + folderName + "_encryptedFiles/";
      File destFolderPathDir = new File(destFolderPath);
      if (!destFolderPathDir.exists()) { 
    	  destFolderPathDir.mkdir();
      }
      if (parent.isDirectory()) {
        try {
          List<File> fileListArr = listFilesForFolder(parent);
          for (int i = 0; i < fileListArr.size(); i++) {
            saveUploadAppDetails(((File)fileListArr.get(i)).getAbsolutePath(), destFolderPathDir.getAbsolutePath());
            ((File)fileListArr.get(i)).delete();
          }
        }
        catch (Exception e) {
          message = "Error - " + e.getMessage();
        }
      }
     
    }
    catch (Exception e)
    {
      message = "Folder does not exist on server";
    }
	return message;
  }
  

  public void getFileListFromFodler()
  {
    try
    {
      System.out.println("Completed");
      System.out.println("ddd");
      System.out.println("12345678901234561234567890123456");

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
