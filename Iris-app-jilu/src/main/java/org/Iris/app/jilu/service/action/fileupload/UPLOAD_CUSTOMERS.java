package org.Iris.app.jilu.service.action.fileupload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.bean.enums.CustomerListType;
import org.Iris.app.jilu.common.bean.enums.JiLuLuaCommand;
import org.Iris.app.jilu.service.action.FileUploadAction;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.session.FileUploadSession;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.Iris.util.io.ExcelUtil;
import org.apache.commons.fileupload.FileItem;
/**
 * 导入客户
 * @author 樊水东
 * 2017年5月15日
 */
public class UPLOAD_CUSTOMERS extends FileUploadAction{

	@Override
	protected String execute0(FileUploadSession session) {
		FileItem item = session.getFileParam("upload_customers");
		MemMerchant memMerchant = (MemMerchant) session.getRequest().getSession().getAttribute("merchant");
		if (memMerchant == null)
			return Result.jsonError(ICode.Code.TOKEN_INVALID);
		List<MemCustomer> customers = new ArrayList<>();
		try {
			ArrayList<ArrayList<Object>> rowlist = ExcelUtil.readExcel(item.getInputStream(), item.getName());
			if (rowlist.size() > 2){
				for (int i = 2; i < rowlist.size(); i++) {
					List<Object> colList = rowlist.get(i);
					if (colList.size() < 5)
						return Result.jsonError(JiLuCode.EXCEL_IMPORT_FAIL);
				}
				for (int i = 2; i < rowlist.size(); i++) {
					List<Object> colList = rowlist.get(i);
					MemCustomer customer = BeanCreator.newMemCustomer(memMerchant.getMerchantId(),
							(String) colList.get(0), (String) colList.get(1), (String) colList.get(2),
							(String) colList.get(3), (String) colList.get(4));
					customers.add(customer);
				}
			}else {
				return Result.jsonError(JiLuCode.EXCEL_IMPORT_FAIL);
			}
			for(MemCustomer customer: customers){
				memCustomerMapper.insert(customer);
				String key = MerchantKeyGenerator.customerDataKey(customer.getMerchantId());
				redisOperate.hset(key, String.valueOf(customer.getCustomerId()), customer.toString());
				String member = String.valueOf(customer.getCustomerId());
				// 尝试将客户添加到商户排序列表中(如果商户排序列表还没有加载，则不会添加)
				long merchantId = customer.getMerchantId();
				luaOperate.evalLua(JiLuLuaCommand.CUSTOMER_LIST_ADD.name(), 5,
						MerchantKeyGenerator.customerListLoadTimeKey(merchantId),
						CustomerListType.PURCHASE_SUM.redisCustomerListKey(merchantId),
						CustomerListType.PURCHASE_RECENT.redisCustomerListKey(merchantId),
						CustomerListType.PURCHASE_FREQUENCY.redisCustomerListKey(merchantId),
						CustomerListType.NAME.redisCustomerListKey(merchantId), member,
						String.valueOf((int) customer.getNamePrefixLetter().charAt(0)));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return Result.jsonError(JiLuCode.EXCEL_IMPORT_FAIL);
		}
		return Result.jsonSuccess(customers.size());
	}

}
