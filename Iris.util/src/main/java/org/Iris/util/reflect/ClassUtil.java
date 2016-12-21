package org.Iris.util.reflect;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);

	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = ClassUtil.class.getClassLoader();
			if (cl == null) {
				// getClassLoader() returning null indicates the bootstrap ClassLoader
				try {
					cl = ClassLoader.getSystemClassLoader();
				}
				catch (Throwable ex) {
					// Cannot access system ClassLoader - oh well, maybe the caller can live with null...
				}
			}
		}
		return cl;
	}
	
	public static List<Class<?>> scanPackage(String pack, boolean inner) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		String path = pack.replaceAll("\\.", "\\/");
		path= getDefaultClassLoader().getResource(path).getPath();
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()){
			logger.error("package {} not exist!", pack);
			return classes;
		}
		scanFile(classes, file, pack, inner);
		return classes;
	}
	
	public static void scanFile(List<Class<?>> classes, File directory, String parentPackage, boolean inner) {
		File[] files = directory.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				String name = file.getName();
				boolean flag = file.isDirectory() || name.endsWith(".class");
				if (!inner) 
					flag = flag && !name.contains("$");
				return flag;
			}
		});
		for (File file : files) {
			String pack = parentPackage + "." + file.getName().replaceAll(".class", "");
			if (file.isDirectory())
				scanFile(classes, file, pack, inner);
			else {
				try {
					Class<?> clazz = Class.forName(pack);
					classes.add(clazz);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException("Class - " + pack + " not found!", e);
				}
			}
		}
	}
}
