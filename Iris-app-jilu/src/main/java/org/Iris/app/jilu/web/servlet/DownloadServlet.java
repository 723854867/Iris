package org.Iris.app.jilu.web.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void  doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	@SuppressWarnings("all")
	protected void  doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		 String filePath = this.getServletContext().getRealPath("/WEB-INF/download/产品模板.xlsx");
		 File f = new File(filePath);
		    if (!f.exists()) {
		      response.sendError(404, "File not found!");
		      return;
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
