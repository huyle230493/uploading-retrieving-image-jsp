package com.demo.image.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sound.midi.Soundbank;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;


@WebServlet(urlPatterns = {"/image"})
@MultipartConfig(maxFileSize = 169999999)
public class ImageController extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String UPLOAD_DIRECTORY = "C:/uploads";
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/jsp/upload.jsp").forward(req, resp);
		//Connection conn = null;
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get all the require fields from upload.jsp page
		try {
			String email = request.getParameter("email");
			Part filePart = request.getPart("file");
			InputStream inputStream = null;
			String base64Image = null;
			if (filePart != null) {
				long fileSize = filePart.getSize();
				String fileContent = filePart.getContentType();
				inputStream = filePart.getInputStream();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                 
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);                  
                }
                 
                byte[] imageBytes = outputStream.toByteArray();
                base64Image = Base64.getEncoder().encodeToString(imageBytes);
                 
                 
                inputStream.close();
                outputStream.close();
			}
			
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			Connection conn = DriverManager
					.getConnection("jdbc:db2://localhost:50000/TESTDB",
									"soe-load",
								    "P@ssword1234567");
			PreparedStatement preparedStatement = conn.prepareStatement(
					"INSERT INTO TESTDBSHOP.ACCOUNT (EMAIL, IMAGE) VALUES (?, ?)");
			preparedStatement.setString(1, email);
			// Only use for jdbc 4.0
			// preparedStatement.setBlob(2, inputStream);
			// This can use for jdbc 3.0
			preparedStatement.setBinaryStream(2, filePart.getInputStream(), (int) filePart.getSize());
			int returnCode = preparedStatement.executeUpdate();
			conn.close();
			System.out.println("Checked returnCode");
			if (returnCode == 0) {
				request.setAttribute("message", "Error inserting file");
				getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
			} else {
				request.setAttribute("message", "Success");
				request.setAttribute("image", base64Image);
				getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		//Don't know how it works yet.
		/*//process only if its multipart content
        if(ServletFileUpload.isMultipartContent(request)){
            try {
                List<FileItem> multiparts = new ServletFileUpload(
                		new DiskFileItemFactory()).parseRequest((RequestContext) request);
                for(FileItem item : multiparts){
                    if(!item.isFormField()){
                        String name = new File(item.getName()).getName();
                        item.write( new File(UPLOAD_DIRECTORY + File.separator + name));
                    }
                }
            
               //File uploaded successfully
               request.setAttribute("message", "File Uploaded Successfully");
            } catch (Exception ex) {
               request.setAttribute("message", "File Upload Failed due to " + ex);
            }          
          
        }else{
            request.setAttribute("message",
                                 "Sorry this Servlet only handles file upload request");
        }
        request.getRequestDispatcher("/result.jsp").forward(request, response);
    }*/
	}
	
	public void read() {
		
	}
	
	public void create() {
		
	}
	
	public void update() {
		
	}
	
	public void delete() {
		
	}
}
