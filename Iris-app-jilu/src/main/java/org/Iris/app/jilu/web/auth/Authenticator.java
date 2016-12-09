package org.Iris.app.jilu.web.auth;

import org.Iris.app.jilu.web.session.IrisSession;

/**
 * 请求验证器
 * 
 * @author Ahab
 */
public interface Authenticator {
	
	/**
	 * 请求验证
	 * 
	 * @param session
	 * @return
	 */
	boolean auth(IrisSession session);
}
