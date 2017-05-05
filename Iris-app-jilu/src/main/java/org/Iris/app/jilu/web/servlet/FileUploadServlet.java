package org.Iris.app.jilu.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.service.action.FileUploadAction;
import org.Iris.app.jilu.web.IrisDispatcher;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.FileUploadSession;
import org.Iris.core.exception.IllegalConstException;

public class FileUploadServlet extends IrisDispatcher<FileUploadSession, FileUploadAction>{

	private static final long serialVersionUID = -1307957341692151832L;

	public FileUploadServlet() {
		super("org.zimo.app.qydj.service.action.fileupload");
	}
	@Override
	protected FileUploadSession buildSession(HttpServletRequest req, HttpServletResponse resp) {
		return new FileUploadSession(req, resp);
	}

	@Override
	protected void receive(FileUploadSession session) {
		FileUploadAction action = actions.get(session.getKVParam(JiLuParams.ACTION));
		if (null == action) 
			throw IllegalConstException.errorException(JiLuParams.ACTION);
		action.execute(session);
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
	}


}
