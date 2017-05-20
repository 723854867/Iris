package org.Iris.app.jilu.service.action.fileupload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.Iris.app.jilu.service.action.FileUploadAction;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.session.FileUploadSession;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.Iris.util.io.ExcelUtil;
import org.apache.commons.fileupload.FileItem;
/**
 * 商户导入产品
 * @author 樊水东
 * 2017年5月15日
 */
public class UPLOAD_GOODS_MERCHANT extends FileUploadAction{

	@Override
	protected String execute0(FileUploadSession session) {
		FileItem item = session.getFileParam("upload_goods");
		MemMerchant memMerchant = (MemMerchant)session.getRequest().getSession().getAttribute("merchant");
		if(memMerchant == null)
			return Result.jsonError(ICode.Code.TOKEN_INVALID);
		long count=0;
		try {
			ArrayList<ArrayList<Object>> rowlist = ExcelUtil.readExcel(item.getInputStream(), item.getName());
			if(rowlist.size()>2){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("list", rowlist);
				map.put("memMerchant", memMerchant);
				count = cfgGoodsMapper.batchInsertByMerchant(map);
			}
			else {
				return Result.jsonError(JiLuCode.EXCEL_IMPORT_FAIL);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Result.jsonError(JiLuCode.EXCEL_IMPORT_FAIL);
		}
		return Result.jsonSuccess(count);
	}

}
