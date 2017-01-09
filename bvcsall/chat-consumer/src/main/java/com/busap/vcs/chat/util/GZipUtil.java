package com.busap.vcs.chat.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.busap.vcs.base.WsMessage;

public class GZipUtil {

	private static Logger logger = LoggerFactory.getLogger(GZipUtil.class);
	public static void main(String[] args) {
		
		List<WsMessage> list = new ArrayList<WsMessage>();
		for(int i=0;i<100;i++){
			WsMessage m = new WsMessage();
			m.setChildCode("1001");
			m.setCode("100");
			m.setSenderId("118991");
			m.setSenderName("这是中文版");
			m.setRoomId("40968");
			m.setContent("来了"+i);
			
			m.getExtra().put("userid", 118991);
			m.getExtra().put("sex", "1");
			m.getExtra().put("name", "这是中文版");
			m.getExtra().put("username", "123456789");
			m.getExtra().put("signature", "sbsbsbsbsbsbsbsbsbsbsbsbsbsb");
			m.getExtra().put("vipStat", "1");
			m.getExtra().put("pic", "tututututututututututututututututuuttutututututututututu");
			
			list.add(m);
		}
		
//		byte b1[] = SerializeUtil.ObjectToByte(list);
//		System.out.println("object size:" + b1.length);
		long t1 = System.currentTimeMillis();
		byte[] buf = GZipUtil.buildChat("1000", list,1);
		long t2 = System.currentTimeMillis();
		System.out.println("time used(ms):"+(t2-t1));
		byte b2[] = buf;//GZipUtil.writeCompressObject(list);
		System.out.println("gzip size:" + b2.length);
		byte data[] = new byte[b2.length-32];
		byte rid[] = new byte[8];
		System.arraycopy(b2, 12, rid, 0, 8);
		System.arraycopy(b2, 32, data, 0, b2.length-32);
		
		System.out.println("room id："+ByteBuffer.wrap(rid).getLong());
		
		String s1 = GZipUtil.readCompressObject(data);
		System.out.println("str size:" + s1.getBytes().length +"  "+s1);
		
		String s = JSONObject.toJSONString(list);
		System.out.println("str size:" + s.getBytes().length +"  "+s);
		
//		byte b3[] = GZipUtil.writeCompressString(s.getBytes());
//		System.out.println("gzip str size:" + b3.length);
	}

	public static byte[] writeCompressObject(List<WsMessage> messages){ 
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
	
	public static byte[] writeCompress(byte bytes[]){ 
		byte[] data_=null; 
		try{ 
			//建立字节数组输出流 
			ByteArrayOutputStream o = new ByteArrayOutputStream(); 
			//建立gzip压缩输出流 
			GZIPOutputStream gzout=new GZIPOutputStream(o); 
			//建立对象序列化输出流
//			ObjectOutputStream out = new ObjectOutputStream(gzout); 
			gzout.write(bytes);
			gzout.flush();
//			out.write(messages.getBytes());//.writeObject(messages); 
//			out.flush(); 
//			out.close(); 
			gzout.close(); 
			//返回压缩字节流 
			data_=o.toByteArray(); 
			o.close(); 
	
		}catch(IOException e){ 
			System.out.println(e); 
		} 
		return data_; 
	} 
	/**
	 * 房间群聊消息合并压缩和封装
	 * @param targetId
	 * @param messages
	 * @param type 0 私聊 ，1 房间内公聊
	 * @return
	 */
	public static byte[] buildChat(String targetId,List<WsMessage> messages,int type){
		if(messages == null || messages.size()<=0){
			logger.warn("empty grouped chat message.");
			return null;
		}
		String s = JSONObject.toJSONString(messages);
		byte sourceData[]=null;
		try {
			sourceData = s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte sourceLength[] = ByteBuffer.allocate(4).putInt(sourceData.length).array();
		
		byte dstData[] = GZipUtil.writeCompress(sourceData);//
		
		byte[] dstLength = ByteBuffer.allocate(4).putInt(dstData.length).array();
		
		byte [] all = new byte[dstData.length+32];
		
		byte h0[] = ByteBuffer.allocate(4).putInt(type).array();//群聊1 私聊0
		System.arraycopy(h0, 0, all, 0, 4);				//[0-3] 2位  群聊1 私聊0
		System.arraycopy(sourceLength, 0, all, 4, 4);	//[4-7] 4位 原数据压缩前长度
		System.arraycopy(dstLength, 0, all, 8, 4);		//[8-11] 4位 压缩后长度
		
		byte b[] = ByteBuffer.allocate(8).putLong(Long.parseLong(targetId)).array();//roomid bytes
		System.arraycopy(b, 0, all, 12, 8);				//[12-19] 8位  房间id或用户id，群聊为房间id,私信为uid
		//all[3]    //20-31 备用
		System.arraycopy(dstData, 0, all, 32, dstData.length);//32位开始为压缩
		
		return all;
	}
	
	/**
	 * 房间群聊消息单条压缩和封装
	 * @param roomId
	 * @param messages
	 * @return
	 */
	public static byte[] buildRoomChat(String roomId,WsMessage messages){
		if(messages != null){
			List<WsMessage> msList = new ArrayList<WsMessage>();
			msList.add(messages);
			return buildChat(roomId,msList,1);
		}else{
			logger.warn("empty chat message.");
			return null;
		}
		
	}
	
	/**
	 * 房间群聊消息单条压缩和封装
	 * @param roomId
	 * @param messages
	 * @return
	 */
	public static byte[] buildUserChat(String uid,WsMessage messages){
		if(messages != null){
			List<WsMessage> msList = new ArrayList<WsMessage>();
			msList.add(messages);
			return buildChat(uid,msList,0);
		}else{
			logger.warn("empty chat message.");
			return null;
		}
		
	}
	
	//将压缩字节数组还原为字符串 
	public static String readCompressObject(byte[] data_){
		String object_=null; 
		try{ 
			//建立字节数组输入流 
			ByteArrayInputStream i = new ByteArrayInputStream(data_); 
			//建立gzip解压输入流 
			GZIPInputStream gzin=new GZIPInputStream(i); 
			//建立对象序列化输入流 
			ObjectInputStream in = new ObjectInputStream(gzin); 
			//按制定类型还原对象 
			object_=(String)in.readObject(); 
			i.close(); 
			gzin.close(); 
			in.close(); 
		}catch(ClassNotFoundException e){ 
			System.out.println(e); 
		}catch(IOException e) { 
			System.out.println(e); 
		} 
		return(object_); 
	} 
	
}
