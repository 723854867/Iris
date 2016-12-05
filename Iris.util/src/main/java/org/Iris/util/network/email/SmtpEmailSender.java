package org.Iris.util.network.email;

public class SmtpEmailSender {

	private int port = 25;				// smtp 服务器默认端口号
	private String host;
	private String from;
	private boolean auth;
	private String username;
	private String password;
	
	
	
	public void init() { 
		
	}
	
	public void setPort(int port) {
		this.port = port;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
