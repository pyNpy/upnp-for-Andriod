package com.example.jettyUnit;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.ServletException;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.handler.*;
 

import org.eclipse.jetty.server.Request;
 
import android.text.TextUtils;

public class ShareFileHandler extends  DefaultHandler{
	public static String srcPath;
	public ShareFileHandler() {
	        super();
	}
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
//		if(response.isCommitted()||baseRequest.isHandled())
//			return;
		
		String method= request.getMethod();
		if(TextUtils.equals("GET", method))
		{

			String filepath=request.getParameter("file");

			java.io.File file=new java.io.File(filepath);
			
			if(!file.exists())			{
				baseRequest.setHandled(true);
				return;
			}
			if(!file.getAbsolutePath().startsWith("/sdcard/")&&!file.getAbsolutePath().startsWith("/storage/")) {
				baseRequest.setHandled(true);
				return ;
			}
			// response.setContentType("image/*");
			response.addHeader("Connection","keep-alive");
			response.addHeader("Accept-Ranges","bytes");
			response.addHeader("todo:", "handle GET msg");
			byte[] buff=new byte[4096];
			FileInputStream  fileInputStream =new FileInputStream(file);
			int count =fileInputStream.read(buff);
			while( count!=-1)
			{
				response.getOutputStream().write(buff,0,count);
				count =fileInputStream.read(buff);
			}
			response.getOutputStream().flush();
			fileInputStream.close();
		}
		baseRequest.setHandled(true); 
	}
	
	
	public void copyFile(String src, String des)
	{
		try {
			copyFileUsingFileChannels(new File(src), new File(des));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void copyFileUsingFileChannels(File source, File dest) throws IOException {    
        FileChannel inputChannel = null;    
        FileChannel outputChannel = null;    
    try {
        inputChannel = new FileInputStream(source).getChannel();
        outputChannel = new FileOutputStream(dest).getChannel();
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
    } finally {
        inputChannel.close();
        outputChannel.close();
    }
}
	
	
	
	
	
	
	
	
}
