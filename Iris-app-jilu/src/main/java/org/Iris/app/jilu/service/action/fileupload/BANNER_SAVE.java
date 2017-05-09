package org.Iris.app.jilu.service.action.fileupload;

import org.Iris.app.jilu.service.action.FileUploadAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.FileUploadSession;
import org.apache.commons.fileupload.FileItem;

public class BANNER_SAVE extends FileUploadAction{

	@Override
	protected String execute0(FileUploadSession session) {
		long id = session.getKVParam(JiLuParams.ID);
		String title = session.getKVParam(JiLuParams.TITLE);
		String summary=session.getKVParam(JiLuParams.SUMMARY);
		FileItem fmItem = session.getFileParam(JiLuParams.FM_URL.key());
		FileItem gdItem = session.getFileParam(JiLuParams.GD_URL.key());
		String href = session.getKVParam(JiLuParams.HREF);
		return fileuploadService.SaveBanner(id,title,summary,fmItem,gdItem,href);
	}

}
