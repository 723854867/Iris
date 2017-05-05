package org.Iris.app.jilu.service.action;

import org.Iris.app.jilu.web.session.FileUploadSession;

public abstract class FileUploadAction implements IAction<FileUploadSession>{

	@Override
	public void execute(FileUploadSession session) {
		session.write(execute0(session));
	}

	protected abstract String execute0(FileUploadSession session);

}
