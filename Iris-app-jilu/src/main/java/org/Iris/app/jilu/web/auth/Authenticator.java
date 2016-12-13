package org.Iris.app.jilu.web.auth;

import org.Iris.app.jilu.web.session.IrisSession;

/**
 * 请求验证器
 * 
 * @author Ahab
 */
public interface Authenticator<SESSION extends IrisSession> {
	
	/**
	 * 请求验证
	 * 
	 * @param session
	 * @return
	 */
	boolean auth(SESSION session);
}
