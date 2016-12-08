package org.Iris.util.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class IpUtil {

	private static final String IP_FORMAT = "(\\d{1,3}\\.){3}\\d{1,3}";

	/**
	 * 获取当前主机 IP 地址
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static String getLocalIP() throws SocketException {
		InetAddress ip = null;
		boolean bFindIP = false;
		Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
				.getNetworkInterfaces();
		while (netInterfaces.hasMoreElements()) {
			if (bFindIP)
				break;
			NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
			Enumeration<InetAddress> ips = ni.getInetAddresses();
			while (ips.hasMoreElements()) {
				ip = (InetAddress) ips.nextElement();
				if (!ip.isLoopbackAddress() && ip.getHostAddress().matches(IP_FORMAT)) {
					bFindIP = true;
					break;
				}
			}
		}
		return null != ip ? ip.getHostAddress() : null;
	}

	/**
	 * 获取当前主机 IP 地址(多网卡)
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static List<String> getLocalIPS() throws SocketException {
		List<String> ipList = new ArrayList<String>();
		Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
				.getNetworkInterfaces();
		while (netInterfaces.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
			Enumeration<InetAddress> ips = ni.getInetAddresses();
			while (ips.hasMoreElements()) {
				InetAddress ip = (InetAddress) ips.nextElement();
				if (!ip.isLoopbackAddress() && ip.getHostAddress().matches(IP_FORMAT))
					ipList.add(ip.getHostAddress());
			}
		}
		return ipList;
	}

	/**
	 * 获取当前主机 mac 地址
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static String getMacAddress() {
		InetAddress ip = null;
		try {
			boolean bFindIP = false;
			NetworkInterface ni = null;
			Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				if (bFindIP) 
					break;
				ni = (NetworkInterface) netInterfaces.nextElement();
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					ip = (InetAddress) ips.nextElement();
					if (!ip.isLoopbackAddress() && ip.getHostAddress().matches(IP_FORMAT)) {
						bFindIP = true;
						break;
					}
				}
			}
			return null != ip ? bytesToHex(ni.getHardwareAddress()) : null;
		} catch (SocketException e) {
			throw new RuntimeException("get mac address failure!", e);
		}
	}

	private static String bytesToHex(byte[] bytes) {
		StringBuffer mac = new StringBuffer();
		byte currentByte;
		boolean first = true;
		for (byte b : bytes) {
			if (!first) 
				mac.append("-");
			currentByte = (byte) ((b & 240) >> 4);
			mac.append(Integer.toHexString(currentByte));
			currentByte = (byte) (b & 15);
			mac.append(Integer.toHexString(currentByte));
			first = false;
		}
		return mac.toString().toUpperCase();
	}
}
