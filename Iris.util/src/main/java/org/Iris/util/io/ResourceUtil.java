package org.Iris.util.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.Iris.util.lang.StringUtil;
import org.Iris.util.reflect.ClassUtil;

public abstract class ResourceUtil {
	
	private static final String CLASSPATH_URL_PREFIX = "classpath:";
	
	private static final String URL_PROTOCOL_FILE = "file";

	public static URL getURL(String resourceLocation) throws FileNotFoundException {
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
			ClassLoader cl = ClassUtil.getDefaultClassLoader();
			URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
			if (url == null) {
				String description = "class path resource [" + path + "]";
				throw new FileNotFoundException(description +
						" cannot be resolved to URL because it does not exist");
			}
			return url;
		}
		try {
			return new URL(resourceLocation);
		} catch (MalformedURLException ex) {
			try {
				return new File(resourceLocation).toURI().toURL();
			}catch (MalformedURLException ex2) {
				throw new FileNotFoundException("Resource location [" + resourceLocation + "] is neither a URL not a well-formed file path");
			}
		}
	}
	
	public static File getFile(String resourceLocation) throws FileNotFoundException {
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
			String description = "class path resource [" + path + "]";
			ClassLoader cl = ClassUtil.getDefaultClassLoader();
			URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
			if (url == null) {
				throw new FileNotFoundException(description + " cannot be resolved to absolute file path because it does not exist");
			}
			return getFile(url, description);
		}
		try {
			return getFile(new URL(resourceLocation));
		} catch (MalformedURLException ex) {
			return new File(resourceLocation);
		}
	}
	
	public static File getFile(URL resourceUrl) throws FileNotFoundException {
		return getFile(resourceUrl, "URL");
	}
	
	public static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
		if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) 
			throw new FileNotFoundException(description + " cannot be resolved to absolute file path " + "because it does not reside in the file system: " + resourceUrl);
		try {
			return new File(toURI(resourceUrl).getSchemeSpecificPart());
		} catch (URISyntaxException ex) {
			return new File(resourceUrl.getFile());
		}
	}
	
	public static URI toURI(URL url) throws URISyntaxException {
		return toURI(url.toString());
	}
	
	public static URI toURI(String location) throws URISyntaxException {
		return new URI(StringUtil.replace(location, " ", "%20"));
	}
}
