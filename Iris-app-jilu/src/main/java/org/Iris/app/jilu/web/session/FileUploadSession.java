package org.Iris.app.jilu.web.session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.core.consts.IrisConst;
import org.Iris.core.exception.IllegalConstException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class FileUploadSession extends IrisSession{

	private Map<String, Object> params = new HashMap<>();
	
	public FileUploadSession(HttpServletRequest request, HttpServletResponse response){
		super(request, response);
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();   
	        ServletFileUpload upload = new ServletFileUpload(factory);   
	        upload.setHeaderEncoding("UTF-8");  
	        List items = upload.parseRequest(request);  
	        for(Object object:items){  
	            FileItem fileItem = (FileItem) object;   
	            if (fileItem.isFormField()) {
	            	params.put(fileItem.getFieldName(), fileItem.getString("utf-8"));//如果你页面编码是utf-8的   
	            } else{
	            	params.put(fileItem.getFieldName(), fileItem);
	            }
	        }  
		} catch (Exception e) {
		}
	}
	
	public <T> T getKVParam(IrisConst<T> constant) throws IllegalConstException {
		String val = (String)params.get(constant.key());
		if (null == val)
			throw IllegalConstException.nullException("param [" + constant.key() + "] missing!", constant);
		try {
			return constant.parse(val);
		} catch (Exception e) {
			throw IllegalConstException.exception("param [" + constant.key() + "] error!", constant);
		}
	}
	
	public <T> T getKVParamOptional(IrisConst<T> constant) {
		try {
			return getKVParam(constant);
		} catch (IllegalConstException e) {
			return constant.defaultValue();
		}
	}
	
	public FileItem getFileParam(String key){
		FileItem fileItem =  (FileItem)params.get(key);
		return fileItem;
	}

}
