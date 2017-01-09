package com.spnetty.util;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class UriUtils {
	static String TCP = "tcp";
	static String UDP = "udp";
	static String HTTP = "http";
	static String FTP = "ftp";
	final static Map<String, Integer> DEFAULT_PORTS = new HashMap<String, Integer>();
	static {
		DEFAULT_PORTS.put(TCP, 63880);
		DEFAULT_PORTS.put(UDP, 53880);
		DEFAULT_PORTS.put(HTTP, 80);
		DEFAULT_PORTS.put(FTP, 21);
	}

	public static Integer getSchemePort(String protol) {
		return DEFAULT_PORTS.get(protol);
	}

	public static String getSchemeHost(String protol) {
		return "127.0.0.1";
	}

	public static URI getURI(String url) {
		URI u = UrlBuilder.fromString(url).toUri();
		return u;
	}

	public static String getHost(URI uri) {
		String host = uri.getHost();
		if (host == null) {
			host = getSchemeHost(uri.getScheme());
		}
		return host;
	}

	public static Integer getPort(URI uri) {
		int port = uri.getPort();
		if (port == -1) {
			port = getSchemePort(uri.getScheme());
		}
		return port;
	}

}
