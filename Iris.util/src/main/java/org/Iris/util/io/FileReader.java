package org.Iris.util.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader {
	
	/**
	 * 适用于读取小型文件
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] bufferRead(String filePath) throws IOException { 
		File file = new File(filePath);
		if (!file.exists())
			throw new FileNotFoundException(filePath);
		if (file.isDirectory())
			throw new IOException("File " + filePath + "is a directory, no data to read!");
		return bufferRead(file);
	}
	
	/**
	 * 适用于读取小型文件
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] bufferRead(File file) throws IOException { 
		BufferedInputStream in = null;
		byte[] buffer = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			buffer = new byte[in.available()];
			in.read(buffer);
		} finally {
			if (null != in)
				in.close();
		}
		return buffer;
	}
	
	/**
	 * 适用于读取小型文件，从 jar 文件读取文件
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static byte[] bufferReadFromClassOrJar(Class<?> clazz, String name) throws IOException { 
		BufferedInputStream in = null;
		byte[] buffer = null;
		try {
			in = new BufferedInputStream(clazz.getResourceAsStream(name));
			buffer = new byte[in.available()];
			in.read(buffer);
		} finally {
			if (null != in)
				in.close();
		}
		return buffer;
	}
}
