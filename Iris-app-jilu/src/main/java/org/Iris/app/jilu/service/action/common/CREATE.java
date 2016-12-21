package org.Iris.app.jilu.service.action.common;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.bean.form.MerchantForm;
import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.service.realm.unit.merchant.MerchantOperator;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;

/**
 * 商户完善信息
 * 
 * @author Ahab
 */
public class CREATE extends CommonAction {
	
	@Override
	protected String execute0(IrisSession session) {
		String name = session.getKVParam(JiLuParams.NAME);
		String address = session.getKVParam(JiLuParams.ADDRESS);
		String token = session.getKVParam(JiLuParams.TOKEN);
		
		AccountModel am = luaOperate.delAndGetHash(CommonKeyGenerator.createMarkDataKey(token), new AccountModel());
		if (null == am)
			throw IllegalConstException.errorException(JiLuParams.TOKEN);
		MerchantOperator operator = unitCache.insertMerchant(BeanCreator.newMemMerchant(name, address), am);
		if (!operator.login(am.getAccount(), true))
			return Result.jsonError(ICode.Code.REQUEST_FREQUENTLY);
		return Result.jsonSuccess(new MerchantForm(operator));
	}
}
