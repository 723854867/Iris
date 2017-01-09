package com.busap.vcs.websocket.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {

	public static byte[] ObjectToByte(Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);

			byte[] bytes = bo.toByteArray();

			bo.close();
			oo.close();
			return bytes;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return null;
	}

	public static Object ByteToObject(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
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

	public static void main(String[] args) {
		String hello =null;

		byte[] objectToByte = SerializeUtil.ObjectToByte(hello);

		String ct = (String) SerializeUtil.ByteToObject(objectToByte);
		System.out.println(ct);
	}
}