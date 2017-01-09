package com.busap.vcs.service.utils;

import com.busap.vcs.service.bean.ResInObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class HttpsUtil {
	// HTTP连接超时的时间 秒
	private String connection_timeout = "30";
	// 从响应中读取数据超时的时间
	private String so_timeout = "30";
	// 提交方式
	private String method = "POST";

	/**
	 * 设置连接超时时间，单位秒，默认为30秒
	 * 
	 * @param connection_timeout
	 */
	public void setConnection_timeout(String connection_timeout) {
		this.connection_timeout = connection_timeout;
	}

	/**
	 * 设置读取超时时间，单位秒，默认为30秒
	 * 
	 * @param so_timeout
	 */
	public void setSo_timeout(String so_timeout) {
		this.so_timeout = so_timeout;
	}

	/**
	 * 设置提交方式为GET，默认为POST
	 */
	public void setMethodIsGet() {
		this.method = "GET";
	}


	/**
	 * 忽视证书HostName
	 */
	private HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
		public boolean verify(String s, SSLSession sslsession) {
			return true;
		}
	};

	/**
	 * Ignore Certification
	 */
	private TrustManager ignoreCertificationTrustManger = new X509TrustManager() {

		private X509Certificate[] certificates;

		public void checkClientTrusted(X509Certificate certificates[],
				String authType) throws CertificateException {
			if (this.certificates == null) {
				this.certificates = certificates;
			}
		}

		public void checkServerTrusted(X509Certificate[] ax509certificate,
				String s) throws CertificateException {
			if (this.certificates == null) {
				this.certificates = ax509certificate;
			}

		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	};

	/**
	 * 发送JSION请求方法 默认为POST方式
	 * 
	 * @param urlStr
	 *            请求地址
	 * @return
	 * @throws Exception
	 */
	public String request(String urlStr,ResInObject in) throws Exception {
		String jsonParam=JsonUtil.convertObject2Json(in);
		String res = "";
		HttpsURLConnection conn = null;
		if (urlStr == null || urlStr.trim().length() == 0
				|| !urlStr.toLowerCase().startsWith("https")) {
			return "";
		}
		try {
			URL url = new URL(urlStr);
			HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
			conn = (HttpsURLConnection) url.openConnection();
			// Prepare SSL Context
			TrustManager[] tm = { ignoreCertificationTrustManger };
			SSLContext sslContext = SSLContext.getInstance("TLSv1");;
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			conn.setSSLSocketFactory(ssf);
			conn.setConnectTimeout(Integer.valueOf(connection_timeout) * 1000);
			conn.setReadTimeout(Integer.valueOf(so_timeout) * 1000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(method);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
			String encoding = Base64.encode("userName:passWord".getBytes());
			conn.setRequestProperty("Authorization", "Basic " + encoding);
			if (in.getRequestSource() != null && in.getRequestSource().trim().length() > 0) {
				conn.setRequestProperty("requestSource", in.getRequestSource());
			}

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (jsonParam != null && jsonParam.trim().length() > 0) {
				out.write(jsonParam.getBytes("UTF-8"));
			}

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + urlStr);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}

}
