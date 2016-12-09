package org.Iris.app.jilu.web.handler;

import java.text.MessageFormat;

import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.consts.IrisConst;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(value = "errorHandler")
public class GsonResultErrorhandler implements ErrorHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GsonResultErrorhandler.class);
	
	@Override
	public void onParamNull(IrisSession session, IllegalConstException exception) {
		IrisConst<?> constant = exception.constant();
		session.write(Result.jsonError(constant.constId(), MessageFormat.format(ICode.Code.PARAM_LACK.defaultValue(), constant.key())));
	}

	@Override
	public void onParamError(IrisSession session, IllegalConstException exception) {
		IrisConst<?> constant = exception.constant();
		session.write(Result.jsonError(constant.constId(), MessageFormat.format(ICode.Code.PARAM_ERROR.defaultValue(), constant.key())));
	}

	@Override
	public void onServerError(IrisSession session, Exception exception) {
		session.write(Result.jsonError(ICode.Code.SYSTEM_ERROR.constId(), ICode.Code.SYSTEM_ERROR.defaultValue()));
		logger.error("Server Error!", exception);
	}
}
