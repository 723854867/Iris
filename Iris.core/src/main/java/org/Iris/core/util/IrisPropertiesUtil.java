package org.Iris.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.Iris.core.exception.IrisIOException;
import org.Iris.util.io.CharsetUtil;
import org.Iris.util.io.ResourceUtil;

public class IrisPropertiesUtil {

	public static IrisProperties load(Class<?> clazz, String file) {
		IrisProperties properties = new IrisProperties();
		InputStreamReader reader = null;
		try {
			InputStream in = clazz.getResourceAsStream(file);
			reader = new InputStreamReader(in, CharsetUtil.UTF_8);
			properties.load(reader);
		} catch (IOException e) {
			throw new IrisIOException(file + " load failure by " + clazz.getName() , e);
		} finally {
			if (null != reader)
				try {
					reader.close();
				} catch (IOException e) {
					throw new IrisIOException(file + " io stream close failure!" , e);
				}
		}
		return properties;
	}
	
	public static IrisProperties load(String file) {
		IrisProperties properties = new IrisProperties();
		InputStreamReader reader = null;
		try {
			File f = ResourceUtil.getFile(file);
			reader = new InputStreamReader(new FileInputStream(f), CharsetUtil.UTF_8);
			properties.load(reader);
		} catch (FileNotFoundException e) {
			throw new IrisIOException(file + " not found!", e);
		} catch (IOException e) {
			throw new IrisIOException(file + " load failure from classpath!" , e);
		} finally {
			if (null != reader)
				try {
					reader.close();
				} catch (IOException e) {
					throw new IrisIOException(file + " io stream close failure!" , e);
				}
		}
		return properties;
	}
	
	public static void load(IrisProperties properties, String file) { 
		InputStream in = null;
		try {
			File f = ResourceUtil.getFile(file);
			in = new FileInputStream(f);
			properties.load(in);
		} catch (FileNotFoundException e) {
			throw new IrisIOException(file + " not found!", e);
		} catch (IOException e) {
			throw new IrisIOException(file + " load failure from classpath!" , e);
		} finally {
			if (null != in)
				try {
					in.close();
				} catch (IOException e) {
					throw new IrisIOException(file + " io stream close failure!" , e);
				}
		}
	}
}
