package org.Iris.app.jilu.service.realm;

import java.util.List;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.bean.form.MerchantForm;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.realm.courier.CourierService;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.service.realm.merchant.MerchantService;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.redis.JiLuLuaOperate;
import org.Iris.app.jilu.storage.redis.RedisCache;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.springframework.stereotype.Service;

@Service
public class CommonService extends RedisCache {
	
	@Resource
	private JiLuLuaOperate luaOperate;
	@Resource
	private CourierService courierService;
	@Resource
	private MerchantService merchantService;
	@Resource
	private MemAccountMapper memAccountMapper;

	/**
	 * 登陆
	 * 
	 * @param type
	 * @param account
	 * @param captch
	 * @return
	 */
	public String login(AccountType type, String account, String captch) { 
		switch (type) {
		case MOBILE:
		case EMAIL:
			if (!courierService.verifyCaptch(type, account, captch))
				return Result.jsonError(JiLuCode.CAPTCHA_ERROR);
			
			Merchant merchant = merchantService.getMerchantByAccount(type, account);
			if (null == merchant)
				merchant = merchantService.createMerchant(account, type);
			if (!merchant.login(account, false))
				return Result.jsonError(ICode.Code.REQUEST_FREQUENTLY);
			MerchantForm merchantForm = new MerchantForm(merchant);
			List<MemAccount> list = memAccountMapper.getByMerchantId(merchantForm.getMerchantId());
			for(MemAccount memAccount : list){
				if(memAccount.getType() == 0){
					merchantForm.setPhone(memAccount.getAccount());
				}
				if(memAccount.getType() == 1){
					merchantForm.setEmail(memAccount.getAccount());
				}
			}
			return Result.jsonSuccess(merchantForm);
		case WECHAT:
			return Result.jsonSuccess();
		default:
			throw IllegalConstException.errorException(JiLuParams.TYPE);
		}
	}
}
