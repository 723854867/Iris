package com.busap.vcs.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BeanUtils {
	public static byte[] obj2Bytes(Object obj) {
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			if (obj == null) {
				return null;
			} else {
				bos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(bos);
				oos.writeObject(obj);
				return bos.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (oos != null) {
					oos.close();
				}
			} catch (Exception e2) {

			}
		}
		return null;
	}

	public static Object byte2Obj(byte[] b) {
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			if (b == null) {
				return null;
			} else {
				bis = new ByteArrayInputStream(b);
				ois = new ObjectInputStream(bis);
				return ois.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}

				if (ois != null) {
					ois.close();
				}
			} catch (Exception e2) {

			}
		}
		return null;
	}

	public static boolean checkParamNotNull(Object... param) {
		for (Object obj : param) {
			if (obj == null) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		/*Message msg = new Message();
		msg.setModule(Module.M1);
		msg.setAction(Action.INSERT);
		byte[] b = BeanUtils.obj2Bytes(msg);
		System.out.println(b.length);
		msg = (Message) BeanUtils.byte2Obj(b);
		System.out.println(msg);*/
		
	}
}
