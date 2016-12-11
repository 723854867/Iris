package org.Iris.app.jilu.service.action.common;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.bean.form.MerchantForm;
import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.service.realm.unit.merchant.Merchant;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;

public class CREATE extends CommonAction {
	
	public static final CREATE INSTANCE						 = new CREATE();
	
	private CREATE() {}

	@Override
	protected String execute0(IrisSession session) {
		String name = session.getKVParam(JiLuParams.NAME);
		String avatar = session.getKVParam(JiLuParams.AVATAR);
		String address = session.getKVParam(JiLuParams.ADDRESS);
		String token = session.getKVParam(JiLuParams.TOKEN);
		
		AccountModel am = luaOperate.hdelAndGet(RedisKeyGenerator.getTokenAccountKey(token), new AccountModel());
		if (null == am)
			throw IllegalConstException.errorException(JiLuParams.TOKEN);
		Merchant merchant = tx.createMerchant(BeanCreator.newMemMerchant(name, avatar, address), am);
		if (!merchant.login(am.getAccount()))
			return Result.jsonError(ICode.Code.REQUEST_FREQUENTLY);
		return Result.jsonSuccess(new MerchantForm(merchant));
	}
}
