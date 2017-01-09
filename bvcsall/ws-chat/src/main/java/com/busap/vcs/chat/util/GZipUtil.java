package com.busap.vcs.chat.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipUtil {
	/**
	 * 压缩
	 * @param messages
	 * @return
	 */
	public static byte[] writeCompressString(String messages){ 
		byte[] data_=null; 
		try{ 
			//建立字节数组输出流 
			ByteArrayOutputStream o = new ByteArrayOutputStream(); 
			//建立gzip压缩输出流 
			GZIPOutputStream gzout=new GZIPOutputStream(o); 
			//建立对象序列化输出流
			ObjectOutputStream out = new ObjectOutputStream(gzout); 
			out.writeObject(messages); 
			out.flush(); 
			out.close(); 
			gzout.close(); 
			//返回压缩字节流 
			data_=o.toByteArray(); 
			o.close(); 
	
		}catch(IOException e){ 
			System.out.println(e); 
		} 
		return data_; 
	} 
	//将压缩字节数组还原为字符串 
	public static String readCompressObject(byte[] data_){
		
        byte[] data = new byte[102400];
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data_);
            GZIPInputStream pIn = new GZIPInputStream(in);
            DataInputStream objIn = new DataInputStream(pIn);
	            int len = 0;
	            int count = 0;
	            while ((count = objIn.read(data, len, 1024)) != -1) {
	                len = len + count;
	            }
	            byte[] trueData = new byte[len];
	            System.arraycopy(data, 0, trueData, 0, len);
            objIn.close();
            pIn.close();
            in.close();
            return new String(trueData,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
		return null;
	    
	} 
	
}
