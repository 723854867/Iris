package org.Iris.app.jilu.web.handler;

import org.Iris.app.jilu.web.IrisSession;
import org.Iris.core.exception.IllegalConstException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 专门用来处理错误的 handler
 * 
 * @author ahab
 */
public interface ErrorHandler {
	
	/**
	 * 当参数不存在时的处理回调
	 * 
	 * @param session
	 */
	void onParamNull(IrisSession session, IllegalConstException exception);

	/**
	 * 当参数错误(包括字符串长度不对，类型转换错误等)时的处理回调
	 * 
	 * @param session
	 */
	void onParamError(IrisSession session, IllegalConstException exception);
	
	/**
	 * 当服务器出现异常错误时被调用
	 * 
	 * @param session
	 */
	void onServerError(IrisSession session, Exception exception);
	
	class DefaultErrorHandler implements ErrorHandler {
		private static final Logger logger = LoggerFactory.getLogger(DefaultErrorHandler.class);
		@Override
		public void onParamNull(IrisSession session, IllegalConstException e) {
			session.write(e.getMessage());
		}

		@Override
		public void onParamError(IrisSession session, IllegalConstException e) {
			session.write(e.getMessage());
		}
		
		@Override
		public void onServerError(IrisSession session, Exception e) {
			logger.error("Server exception!", e);
			session.write(e.getMessage());
		}
	}
}
