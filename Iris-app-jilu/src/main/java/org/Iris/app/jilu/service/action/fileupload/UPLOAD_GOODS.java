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
 * 
 * @author 樊水东 2017年5月15日
 */
public class UPLOAD_GOODS extends FileUploadAction {

	@Override
	protected String execute0(FileUploadSession session) {
		FileItem item = session.getFileParam("upload_goods");
		long count=0;
		try {
			ArrayList<ArrayList<Object>> rowlist = ExcelUtil.readExcel(item.getInputStream(), item.getName());
			if(rowlist.size()>2){
				
				//	把列表分开2种情况进行处理
				ArrayList<ArrayList<Object>> updatelist = new ArrayList<ArrayList<Object>>();
				ArrayList<ArrayList<Object>> insertlist = new ArrayList<ArrayList<Object>>();
				
				for (int i = 2;i< rowlist.size();i++) {
					ArrayList<Object> row = rowlist.get(i);

					//	如果sku字段不为空，放入updatelist
					//	否则放入insertlist
					if (row.size() > 4 && row.get(3).toString().length() > 0){
						updatelist.add(row);
					}else{
						insertlist.add(row);
					}
				}
				if (insertlist.size() > 0)
					count += cfgGoodsMapper.batchInsert(insertlist);
				
				if (updatelist.size() > 0)
					count += cfgGoodsMapper.batchUpdate(updatelist) / 2;
				
				
				//count = cfgGoodsMapper.batchInsert(rowlist);
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
