package org.Iris.app.jilu.web.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.common.AppConfig;

public class DownloadServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void  doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	@SuppressWarnings("all")
	protected void  doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		 String filePath="";
		 String type = req.getParameter("type");
		 if(type.equals("customer")){
			 filePath = this.getServletContext().getRealPath("/WEB-INF/download/客户模板.xlsx");
		 }else if(type.equals("goods")){
			 filePath = this.getServletContext().getRealPath("/WEB-INF/download/产品模板.xlsx");
		 }else if(type.equals("label")){
			 filePath = this.getServletContext().getRealPath("/WEB-INF/download/标签生成.txt");
		 }
		 File f = new File(filePath);
	    if (!f.exists()) {
	      response.sendError(404, "File not found!");
	      return;
	    }
	    if(type.equals("label")){
	    	//写入标签信息
	    	FileWriter fw=new FileWriter(f);
	    	PrintWriter pw = new PrintWriter(fw);
	    	int count = Integer.valueOf(req.getParameter("count"));
	    	for(int i=0;i<count;i++){
	    		pw.println(AppConfig.getLabelPathPrefix()+"&labelId="+UUID.randomUUID().toString().replace("-", ""));
	    	}
	    	fw.close();
	    	pw.close();
	    }
	    BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
	    byte[] buf = new byte[1024];
	    int len = 0;
	    response.setContentType(getServletContext().getMimeType(f.getName()));
	    response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(f.getName(), "UTF-8"));  
	    OutputStream out = response.getOutputStream();
	    while ((len = br.read(buf)) > 0)
	      out.write(buf, 0, len);
	    br.close();
	    out.close();
	}

}
