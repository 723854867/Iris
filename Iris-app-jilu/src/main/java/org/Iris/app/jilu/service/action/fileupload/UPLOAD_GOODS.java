package org.Iris.app.jilu.service.action.fileupload;

import java.io.IOException;
import java.util.ArrayList;

import org.Iris.app.jilu.service.action.FileUploadAction;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.session.FileUploadSession;
import org.Iris.core.service.bean.Result;
import org.Iris.util.io.ExcelUtil;
import org.apache.commons.fileupload.FileItem;
/**
 * 导入产品
 * @author 樊水东
 * 2017年5月15日
 */
public class UPLOAD_GOODS extends FileUploadAction{

	@Override
	protected String execute0(FileUploadSession session) {
		FileItem item = session.getFileParam("upload_goods");
		long count=0;
		try {
			ArrayList<ArrayList<Object>> rowlist = ExcelUtil.readExcel(item.getInputStream(), item.getName());
			if(rowlist.size()>2)
				 count = cfgGoodsMapper.batchInsert(rowlist);
			else {
				return Result.jsonError(JiLuCode.ACCESSTOKEN_ERROR);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Result.jsonError(JiLuCode.ACCESSTOKEN_ERROR);
		}
		return Result.jsonSuccess(count);
	}

}
