package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import pureclass.Publish_App;

@WebServlet({"/PublishAppServlet"})
@MultipartConfig(fileSizeThreshold=16291456, maxFileSize=1000485760L, maxRequestSize=1000971520L)
public class apps
  extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  private File file;
  private static final String UPLOAD_DIR = "uploads";
  
  public apps() {}
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String action = request.getParameter("action");
    HttpSession session = request.getSession();
    System.out.println("doGet : " + request.getParameter("action"));
  }
  


  private String getFileName(Part part)
  {
    System.out.println("getFileName - " + part.getHeader("content-disposition").toString());
    for (String content : part.getHeader("content-disposition").split(";")) {
      if (content.trim().startsWith("filename"))
        return content.substring(content.indexOf("=") + 2, content.length() - 1);
    }
    return null;
  }
  
  private void deleteDir(File file) {
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
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.setContentType("text/html");
    response.setCharacterEncoding("UTF-8");    
   //  System.setProperty("file.encoding", "UTF-8");

    
    try
    {
      String folderName = request.getParameter("folderName");
      String action = request.getParameter("action");
      System.out.println("action "+action);
      System.out.println("folderName "+folderName);
      
      Publish_App pa = new Publish_App();
      if (action != null && action.equals("deletefolder")) {
    	  
    	  	System.out.println("deletefolder ");
    	  	String uploadPath = getServletContext().getRealPath("") + "uploads/";
            File uploadDir = new File(uploadPath);
            uploadPath = uploadDir.getAbsolutePath() + "/files/";
            uploadDir = new File(uploadPath);
            deleteDir(uploadDir);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
    		out.write("Done");
      }
      else {
        String uploadPath = getServletContext().getRealPath("") + "uploads/";
        File uploadDir = new File(uploadPath);
        String uploadedPath = "";
        if (!uploadDir.exists()) uploadDir.mkdir();
        uploadPath = uploadDir.getAbsolutePath() + "/files/";
        uploadDir = new File(uploadPath);
        //deleteDir(uploadDir);
        if (!uploadDir.exists()) { 
        	uploadDir.mkdir();
        }
				       
        /*String[]entries = uploadDir.list();
        for(String s: entries){
            File currentFile = new File(uploadDir.getPath(),s);
            currentFile.delete();
        }*/
        
        for (Part part : request.getParts())
        {
        	
        	
          String fileName = getFileName(part);
          if (fileName != null) {
        	 // String filename = new String(part.getSubmittedFileName().getBytes("ISO-8859-1"), "UTF-8");
        	 // System.out.println("filename "+filename);

            fileName = fileName.split("/")[1];
            
            part.write(uploadPath + fileName);
            uploadedPath = uploadPath + fileName;
          }
        }
        
       // System.out.println("uploadedPath "+uploadedPath);
        
        String DestFolderPath = uploadPath + "/" + folderName + "/";
        File DestFolder = new File(DestFolderPath);
        if (!DestFolder.exists()) {
        	DestFolder.mkdir();
        }
        
        String returnFilePath = pa.saveUploadAppDetails(uploadedPath, DestFolder.getAbsolutePath());
        String[] splitPath = returnFilePath.split("uploads");
        System.out.println("returnFilePath ");
        System.out.println(returnFilePath);
        response.setCharacterEncoding("UTF-8");

       // response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		out.write("<a href='uploads/"+splitPath[1]+"' target='_blank' download>Download</a>");

      }
      


    }
    catch (Exception e)
    {


      e.printStackTrace();
    }
  }
}
