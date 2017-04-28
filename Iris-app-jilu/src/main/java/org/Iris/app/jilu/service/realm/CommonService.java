package org.Iris.app.jilu.service.realm;

import java.util.List;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.bean.form.MerchantForm;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.realm.courier.CourierService;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.service.realm.merchant.MerchantService;
import org.Iris.app.jilu.service.realm.weixin.WeiXinService;
import org.Iris.app.jilu.service.realm.weixin.result.WeiXinAccessTokenResult;
import org.Iris.app.jilu.storage.domain.CmsVersion;
import org.Iris.app.jilu.storage.domain.MemAccid;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.mybatis.mapper.BgConfigMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.CmsVersionMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.storage.redis.JiLuLuaOperate;
import org.Iris.app.jilu.storage.redis.RedisCache;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.Iris.redis.model.EXPX;
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
	@Resource
	private WeiXinService weiXinService;
	@Resource
	private BgConfigMapper bgConfigMapper;
	@Resource
	private CmsVersionMapper cmsVersionMapper;
	/**
	 * 登陆
	 * 
	 * @param type
	 * @param account
	 * @param captch
	 * @return
	 * @throws Exception 
	 */
	public String login(AccountType type, String account, String captch){ 
//		String accessToken = null;
		switch (type) {
		case MOBILE:
		case EMAIL:
			if (!courierService.verifyCaptch(type, account, captch))
				return Result.jsonError(JiLuCode.CAPTCHA_ERROR);
			break;
		case WECHAT:
//			accessToken = redisOperate.get(CommonKeyGenerator.weiXinAccessTokenKey(account));
//			if(accessToken == null){
//				String freshToken = redisOperate.get(CommonKeyGenerator.weiXinRefreshTokenKey(account));
//				if(freshToken == null)
//					return Result.jsonError(JiLuCode.WEIXIN_ACCESSTOKEN_EXPAIRED);
//				accessToken = refreshAccessToken(freshToken);
//			}else{
//				if(!accessToken.equals(captch))
//					return Result.jsonError(JiLuCode.ACCESSTOKEN_ERROR);
//			}
			break;
		default:
			throw IllegalConstException.errorException(JiLuParams.TYPE);
		}
		
		Merchant merchant = merchantService.getMerchantByAccount(type, account);
		if (null == merchant)
			merchant = merchantService.createMerchant(account, type);
		if (!merchant.login(account, false))
			return Result.jsonError(ICode.Code.REQUEST_FREQUENTLY);
		//MerchantForm merchantForm = getMerchantForm(merchant);
		//merchantForm.setAccessToken(accessToken);
		return Result.jsonSuccess(getMerchantForm(merchant));
		
	}
	private MerchantForm getMerchantForm(Merchant merchant) {
		MerchantForm merchantForm = new MerchantForm(merchant);
		List<MemAccount> list = memAccountMapper.getByMerchantId(merchantForm.getMerchantId());
		for(MemAccount memAccount : list){
			switch (memAccount.getType()) {
			case 0:
				merchantForm.setPhone(memAccount.getAccount());
				break;
			case 1:
				merchantForm.setEmail(memAccount.getAccount());
				break;
			case 2:
				merchantForm.setOpenId(memAccount.getAccount());
				break;
			default:
			}
		}
		//加网易云信账号信息
		MemAccid memAccid = merchant.getMemAccid();
		merchantForm.setAccid(memAccid.getAccid());
		merchantForm.setAccidToken(memAccid.getToken());
		return merchantForm;
	}
	/**
	 * 通过客户端传入的code获取微信接口调用token
	 * @param code
	 * @return
	 */
	public String getAccessToken(String code){
		WeiXinAccessTokenResult result = weiXinService.getAccessToken(code);
		if(result.getAccess_token()!=null){
			redisOperate.setnxpx(CommonKeyGenerator.weiXinAccessTokenKey(result.getOpenid()), result.getAccess_token(), null, EXPX.EX, Long.valueOf(result.getExpires_in()));
			//refreshToken 微信服务器30天过去 我们这里设置29天过期
			redisOperate.setnxpx(CommonKeyGenerator.weiXinRefreshTokenKey(result.getOpenid()), result.getRefresh_token(), null, EXPX.EX, 29*24*60*60);
			return Result.jsonSuccess(result); 
		}else{
			return Result.jsonError(result.getErrcode(), result.getErrmsg());
		}
		
	}
	
	public String refreshAccessToken(String freshToken){
		WeiXinAccessTokenResult result = weiXinService.refreshAccessToken(freshToken);
		if(result.getAccess_token()!=null){
			redisOperate.setnxpx(CommonKeyGenerator.weiXinAccessTokenKey(result.getOpenid()), result.getAccess_token(), null, EXPX.EX, Long.valueOf(result.getExpires_in()));
			//refreshToken 微信服务器30天过去 我们这里设置29天过期
			redisOperate.setnxpx(CommonKeyGenerator.weiXinRefreshTokenKey(result.getOpenid()), result.getRefresh_token(), null, EXPX.EX, 29*24*60*60);
			return result.getAccess_token();
		}else{
			throw IllegalConstException.errorException(JiLuCode.REFRESH_TOKEN_FAIL);
		}
	}
	/**
	 * 获取最新版本
	 */
	public String versionGet(int operatSys){
		CmsVersion cmsVersion = getHashBean(new CmsVersion());
		if(cmsVersion==null){
			cmsVersion = cmsVersionMapper.recentVersion(operatSys);
			if(cmsVersion!=null)
			flushHashBean(cmsVersion);
		}
		return Result.jsonSuccess(cmsVersion);
	}
}
