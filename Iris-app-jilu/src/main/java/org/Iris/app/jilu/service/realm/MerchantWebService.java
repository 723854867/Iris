package org.Iris.app.jilu.service.realm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.bean.form.GoodsPagerForm;
import org.Iris.app.jilu.common.bean.form.Pager;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.common.model.Env;
import org.Iris.app.jilu.service.realm.jms.JmsService;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.domain.SysPage;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.storage.redis.WebKeyGenerator;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.Iris.util.common.KeyUtil;
import org.Iris.util.lang.DateUtils;
import org.springframework.stereotype.Service;

@Service
public class MerchantWebService implements Beans{
	
	@Resource
	private JmsService jmsService;

	/**
	 * 生成验证码: 验证码和 账号对应
	 * 
	 * @param type
	 * @param account
	 * @return
	 */
	public String generateCaptcha(AccountType type, String account) {
		String captchaKey = WebKeyGenerator.accountCaptchaKey(type, account);
		String captchaCountKey = WebKeyGenerator.accountCaptchaCountKey(type, account);
		
		// 生成验证码并且缓存验证码
		String captcha = KeyUtil.randomCaptcha(AppConfig.getCaptchaDigit());
		long flag = luaOperate.recordCaptcha(captchaKey, captchaCountKey, captcha, 
				AppConfig.getCaptchaLifeTime(), AppConfig.getCaptchaCountMaximum(), AppConfig.getCaptchaCountLifeTime());
		if (-1 == flag) 
			return Result.jsonError(JiLuCode.CAPTCHA_GET_CD);
		if (-2 == flag)
			return Result.jsonError(JiLuCode.CAPTCHA_COUNT_LIMIT);
		Env env = AppConfig.getEnv();
		switch (env) {
		case LOCAL:											// 测试环境下直接返回验证码
		case TEST:
			//jmsService.sendCaptchaMessage(type, account, captcha);
			return Result.jsonSuccess(captcha);				
		case ONLINE:										// 线上环境需要发送短信
			jmsService.sendCaptchaMessage(type, account, captcha);
			return Result.jsonSuccess();					
		default:
			return Result.jsonError(ICode.Code.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 登陆
	 * 
	 * @param type
	 * @param account
	 * @param captch
	 * @return
	 * @throws Exception 
	 */
	public String login(AccountType type, String account, String captch,HttpServletRequest request){ 
		switch (type) {
		case MOBILE:
		case EMAIL:
			if (!verifyCaptch(type, account, captch))
				return Result.jsonError(JiLuCode.CAPTCHA_ERROR);
			break;
		default:
			throw IllegalConstException.errorException(JiLuParams.TYPE);
		}
		
		Merchant merchant = merchantService.getMerchantByAccount(type, account);
		if(merchant == null)
			return Result.jsonError(JiLuCode.ACOUNT_IS_NOT_EXIST);
		HttpSession session = request.getSession();
		session.setAttribute("merchant",merchant.getMemMerchant());
		return Result.jsonSuccess(merchant.getMemMerchant());
		
	}
	/**
	 * 检查验证码
	 * 
	 * @param type
	 * @param account
	 * @return
	 */
	public boolean verifyCaptch(AccountType type, String account, String captch) {
		return luaOperate.delIfEquals(WebKeyGenerator.accountCaptchaKey(type, account), captch);
	}
	
	/**
	 * 产品列表
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String getGoodsList(int page, int pageSize,String zhName,String alias,String goodsCode,MemMerchant memMerchant) {
		Map<String, Object> map = new HashMap<>();
		map.put("start", (page-1)*pageSize);
		map.put("pageSize", pageSize);
		map.put("zhName", zhName);
		map.put("alias", alias);
		map.put("goodsCode", goodsCode);
		map.put("source", memMerchant.getMerchantId());
		long count = cfgGoodsMapper.getCount(map);
		if(count == 0)
			return Result.jsonSuccess(Pager.EMPTY);
		List<CfgGoods> list = cfgGoodsMapper.getGoodsList(map);
		for(CfgGoods cfgGoods :list)
			cfgGoods.setUpdateTime(DateUtils.getUTCDate((long)cfgGoods.getUpdated()*1000));
		return Result.jsonSuccess(new Pager<CfgGoods>(count, list));
	}
	
	/**
	 * 插入商品
	 * 
	 * @param memGoods
	 */
	public String insertGoods(CfgGoods memGoods) {
		cfgGoodsMapper.insert(memGoods);
		redisOperate.hmset(memGoods.redisKey(), memGoods);
		return Result.jsonSuccess(memGoods);
	}

	/**
	 * 查看商品信息
	 * 
	 * @param goodsId
	 * @return
	 */
	public String getGoodsInfo(long goodsId) {
		CfgGoods goods = getGoodsById(goodsId);
		if (goods == null)
			throw IllegalConstException.errorException(JiLuParams.GOODS_ID);
		return Result.jsonSuccess(goods);
	}
	
	/**
	 * 更新商品
	 * 
	 * @param memGoods
	 */
	public String updateGoods(long goodsId, String zhName, String usName, String goodsFormat, String classification,
			String zhBrand, String usBrand, String unit, float weight, String alias, String barcode, String sku,
			float unitPrice) {
		CfgGoods memGoods = getGoodsById(goodsId);
		if (memGoods == null)
			throw IllegalConstException.errorException(JiLuParams.GOODS_ID);
		if (zhName != null)
			memGoods.setZhName(zhName);
		if (usName != null)
			memGoods.setUsName(usName);
		if (goodsFormat != null)
			memGoods.setGoodsFormat(goodsFormat);
		if (classification != null)
			memGoods.setClassification(classification);
		if (zhBrand != null)
			memGoods.setZhBrand(zhBrand);
		if (usBrand != null)
			memGoods.setUsBrand(usBrand);
		if (unit != null)
			memGoods.setUnit(unit);
		if (weight != 0)
			memGoods.setWeight(weight);
		if (alias != null)
			memGoods.setAlias(alias);
		if (barcode != null)
			memGoods.setBarcode(barcode);
		if (sku != null)
			memGoods.setSku(sku);
		if (unitPrice != 0)
			memGoods.setUnitPrice(unitPrice);
		int time = DateUtils.currentTime();
		memGoods.setUpdated(time);
		cfgGoodsMapper.update(memGoods);
		redisOperate.hmset(memGoods.redisKey(), memGoods);
		return Result.jsonSuccess(new GoodsPagerForm(memGoods));
	}
	
	/**
	 * 删除商品
	 * 
	 * @param goodsId
	 * @return
	 */
	public String removeGoods(long goodsId) {
		CfgGoods goods = getGoodsById(goodsId);
		if (goods == null)
			throw IllegalConstException.errorException(JiLuParams.GOODS_ID);
		cfgGoodsMapper.delete(goods);
		redisOperate.del(goods.redisKey());
		return Result.jsonSuccess();
	}
	
	/**
	 * 获取商品
	 * @param goodsId
	 * @return
	 */
	public CfgGoods getGoodsById(long goodsId) {
		String key = CommonKeyGenerator.getMemGoodsKey(goodsId);
		CfgGoods goods = redisOperate.hgetAll(key, new CfgGoods(goodsId));
		if (goods != null)
			return goods;
		goods = cfgGoodsMapper.getGoodsById(goodsId);
		if (null != goods)
			redisOperate.hmset(key, goods);
		return goods;
	}
	
	
	/**
	 * 获取菜单列表 sam
	 * 
	 * @return
	 */
	public String getMenuList() {
		return Result.jsonSuccess(webMenuMapper.getSysMenuList());
	}
	
	/**
	 * 获取当前页面父页面路径 sam
	 * 
	 * @return
	 */
	public String GetParentPagePath(String curPagePath) {
		String parentPagePath = "";
		SysPage page = webMenuMapper.getPageByPagePath(curPagePath);
		
		if (page !=null && page.getParentpageid() > 0) {
			page = webMenuMapper.getPageByPageId(page.getParentpageid());
			parentPagePath = page.getUrl();
		} else {
			parentPagePath = page.getUrl();
		}

		return Result.jsonSuccess(parentPagePath);
	}

	public String getMerchantCustomersByNameOrPhone(int page, int pageSize, String name, String phone,MemMerchant memMerchant) {
		Map<String, Object> map = new HashMap<>();
		map.put("start", (page-1)*pageSize);
		map.put("pageSize", pageSize);
		map.put("name", name);
		map.put("mobile", phone);
		map.put("merchantId", memMerchant.getMerchantId());
		long count = memCustomerMapper.getMerchantCustomersCount(map);
		if(count == 0)
			return Result.jsonSuccess(Pager.EMPTY);
		List<MemCustomer> list = memCustomerMapper.getMerchantCustomersByMap(map);
		return Result.jsonSuccess(new Pager<MemCustomer>(count, list));
	}
}