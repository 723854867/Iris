package com.busap.vcs.websocket.bean;

import io.netty.handler.codec.http.HttpRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.busap.vcs.websocket.util.HttpUtil;


public class Base {
	Logger logger = Logger.getLogger(Base.class);
	
	
	private Map<String, String> params = new HashMap<String, String>();
	public Base(HttpRequest request){
		params = HttpUtil.parseUri(request.getUri());
	}
	public Map<String, String> getParams() {
		return params;
	}

	public static Object ByteToObject(byte[] bytes) {
		Object obj = new java.lang.Object();
		try {
			// bytearray to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);

			obj = oi.readObject();

			bi.close();
			oi.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static byte[] ObjectToByte(Object obj) {
		byte[] bytes = new byte[1024];
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);

			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}

}
