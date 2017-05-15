package org.Iris.app.jilu.service.realm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.bean.form.AssumeRoleForm;
import org.Iris.app.jilu.common.bean.form.CzLogForm;
import org.Iris.app.jilu.common.bean.form.GoodsPagerForm;
import org.Iris.app.jilu.common.bean.form.LabelApplyForm;
import org.Iris.app.jilu.common.bean.form.Pager;
import org.Iris.app.jilu.storage.domain.BgUser;
import org.Iris.app.jilu.storage.domain.BuyLabelLog;
import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.CmsBanner;
import org.Iris.app.jilu.storage.domain.CmsVersion;
import org.Iris.app.jilu.storage.domain.MemLabelBind;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.domain.MemPayInfo;
import org.Iris.app.jilu.storage.domain.SysPage;
import org.Iris.app.jilu.storage.redis.BgkeyGenerator;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.IrisSecurity;
import org.Iris.util.lang.DateUtils;
import org.springframework.stereotype.Service;

@Service
public class BackstageService implements Beans{

	/**
	 * 登陆
	 * @param account
	 * @param password
	 * @return
	 */
	public String login(String account, String password,HttpServletRequest request) {
		BgUser user = getBgUser(account);
		if(user == null)
			throw IllegalConstException.errorException(JiLuParams.ACCOUNT);
		if(!user.getPassword().equals(IrisSecurity.toMd5(password)))
			throw IllegalConstException.errorException(JiLuParams.PASSWORD);
		user.setLastLoginTime(DateUtils.currentTime());
		user.setUpdated(DateUtils.currentTime());
		bgUserMapper.update(user);
		redisOperate.hsetByJson(BgkeyGenerator.bgUserDataKey(), account, user);
		
		HttpSession session = request.getSession();
		session.setAttribute("account", account);

		return Result.jsonSuccess(user);
	}
	
	public BgUser getBgUser(String account){
		BgUser bgUser = redisOperate.hgetBean(BgkeyGenerator.bgUserDataKey(), account, BgUser.class);
		if(bgUser==null){
			bgUser = bgUserMapper.find(account);
			if(null != bgUser)
				redisOperate.hsetByJson(BgkeyGenerator.bgUserDataKey(), account, bgUser);
		}
		return bgUser;
	}

	/**
	 * 修改密码
	 * @param account
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	public String updatePwd(String account, String oldPwd, String newPwd) {
		BgUser user = getBgUser(account);
		if(user == null)
			throw IllegalConstException.errorException(JiLuParams.ACCOUNT);
		if(!user.getPassword().equals(IrisSecurity.toMd5(oldPwd)))
			throw IllegalConstException.errorException(JiLuParams.OLDPWD);
		user.setPassword(IrisSecurity.toMd5(newPwd));
		user.setUpdated(DateUtils.currentTime());
		bgUserMapper.update(user);
		redisOperate.hsetByJson(BgkeyGenerator.bgUserDataKey(), account, user);
		return Result.jsonSuccess(user);
	}

	/**
	 * 获取菜单列表 sam
	 * 
	 * @return
	 */
	public String GetMenuList() {
		return Result.jsonSuccess(sysMenuMapper.getSysMenuList());
	}
	
	/**
	 * 获取当前页面父页面路径 sam
	 * 
	 * @return
	 */
	public String GetParentPagePath(String curPagePath) {
		String parentPagePath = "";
		SysPage page = sysMenuMapper.getPageByPagePath(curPagePath);
		
		if (page !=null && page.getParentpageid() > 0) {
			page = sysMenuMapper.getPageByPageId(page.getParentpageid());
			parentPagePath = page.getUrl();
		} else {
			parentPagePath = page.getUrl();
		}

		return Result.jsonSuccess(parentPagePath);
	}

	/**
	 * 修改配置
	 * @param key
	 * @param value
	 * @return
	 */
	public String updateConfig(String key, String value) {
		bgConfigMapper.update(key,value);
		redisOperate.hset(CommonKeyGenerator.bgConfigDataKey(), key, value);
		return Result.jsonSuccess();
	}
	
	/**
	 * 获取所有版本
	 */
	public String versionGet(int pageIndex, int pageSize) {
		return Result.jsonSuccess(cmsVersionMapper.getVersions((pageIndex-1)*pageSize,pageSize));
	}

	/**
	 * 添加版本
	 */
	public String addVersion(String versionNum, int status,String content,String downloadUrl,int operatSys) {
		CmsVersion cmsVersion = new CmsVersion(versionNum, status, content, downloadUrl, operatSys);
		cmsVersionMapper.insert(cmsVersion);
		redisOperate.del(cmsVersion.redisKey());
		return Result.jsonSuccess();
	}

	/**
	 * 版本修改
	 */
	public String updateVersion(long versionId, String versionNum, int status, int delFlag, String content, String downloadUrl, int operatSys){
		CmsVersion cmsVersion = new CmsVersion(versionId, versionNum, status, delFlag, content, downloadUrl, operatSys);
		CmsVersion cmsVer = redisOperate.hgetAll(cmsVersion.redisKey(), new CmsVersion());
		if(cmsVer!=null)
			if(versionId==cmsVer.getVersionId())
				redisOperate.del(cmsVersion.redisKey());
		cmsVersionMapper.update(cmsVersion);
		return Result.jsonSuccess();
	}
	/**
	 * 版本删除
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public String delVersion(long versionId, int operatSys){
		if(versionId==redisOperate.hgetAll(CommonKeyGenerator.getVersion(operatSys), new CmsVersion()).getVersionId())
			redisOperate.del(CommonKeyGenerator.getVersion(operatSys));
		cmsVersionMapper.delete(DateUtils.currentTime(), versionId);
		return Result.jsonSuccess();
	}

	public String getConfigValue(String key){
		String value = redisOperate.hget(CommonKeyGenerator.bgConfigDataKey(), key);
		if(null == value)
			value = bgConfigMapper.findByKey(key);
		if(value !=null)
			redisOperate.hset(CommonKeyGenerator.bgConfigDataKey(), key, value);
		return value;
	}

	public String labelApplyList(int page, int pageSize,int status) {
		Map<String, Object> map = new HashMap<>();
		map.put("start", (page-1)*pageSize);
		map.put("pageSize", pageSize);
		map.put("status", status);
		long count = buyLabelLogMapper.count(map);
		if(count ==0)
			return Result.jsonSuccess(Pager.EMPTY);
		List<BuyLabelLog> list = buyLabelLogMapper.list(map);
		List<LabelApplyForm> forms = new ArrayList<>();
		for(BuyLabelLog labelLog : list){
			MemMerchant memMerchant = merchantService.getMerchantById(labelLog.getMerchantId()).getMemMerchant();
			LabelApplyForm form = new LabelApplyForm();
			form.setId(labelLog.getId());
			form.setMerchantName(memMerchant.getName());
			form.setCount(labelLog.getCount());
			form.setSendName(memMerchant.getSendName());
			form.setSendAddress(memMerchant.getSendAddress());
			form.setSendMobile(memMerchant.getSendMobile());
			form.setCreated(DateUtils.getUTCDate((long)labelLog.getCreated()*1000));
			if(status == 1)
				form.setSendTime(DateUtils.getUTCDate((long)labelLog.getSendTime()*1000));
			forms.add(form);
		}
		return Result.jsonSuccess(new Pager<LabelApplyForm>(count, forms));
	}

	/**
	 * 发货（标签）
	 * @param id
	 * @return
	 */
	public String sendLabel(long id) {
		BuyLabelLog labelLog = buyLabelLogMapper.findById(id);
		if(null == labelLog)
			throw IllegalConstException.errorException(JiLuParams.ID);
		List<MemLabelBind> memLabelBinds = new ArrayList<>();
		long count = labelLog.getCount();
		int time = DateUtils.currentTime();
		for(int i = 0 ; i < count ; i++){
			MemLabelBind memLabelBind = new MemLabelBind();
			memLabelBind.setLabelId(UUID.randomUUID().toString().replace("-", ""));
			memLabelBind.setMerchantId(labelLog.getMerchantId());
			memLabelBind.setBuyId(labelLog.getId());
			memLabelBind.setCreated(time);
			memLabelBind.setUpdated(time);
			memLabelBinds.add(memLabelBind);
		}
		labelLog.setStatus(1);
		labelLog.setSendTime(DateUtils.currentTime());
		buyLabelLogMapper.update(labelLog);
		memLabelBindMapper.batchInsert(memLabelBinds);
		return Result.jsonSuccess();
	}

	/**
	 * 产品列表
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String getGoodsList(int page, int pageSize,String zhName,String alias,String goodsCode) {
		Map<String, Object> map = new HashMap<>();
		map.put("start", (page-1)*pageSize);
		map.put("pageSize", pageSize);
		map.put("zhName", zhName);
		map.put("alias", alias);
		map.put("goodsCode", goodsCode);
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
	 * 商户列表
	 * @param page
	 * @param pageSize
	 * @param name
	 * @return
	 */
	public String getMerchantList(int page, int pageSize, String name) {
		Map<String, Object> map = new HashMap<>();
		map.put("start", (page-1)*pageSize);
		map.put("pageSize", pageSize);
		map.put("name", name);
		long count = memMerchantMapper.count(map);
		if(count == 0)
			return Result.jsonSuccess(Pager.EMPTY);
		List<MemMerchant> list = memMerchantMapper.list(map);
		for(MemMerchant memMerchant :list)
			memMerchant.setLastLoginTime(DateUtils.getUTCTime(memMerchant.getLastLoginTime()));
		return Result.jsonSuccess(new Pager<MemMerchant>(count, list));
	}

	/**
	 * 黑名单操作
	 * @param type
	 * @param merchantId
	 * @return
	 */
	public String merchantOperation(int type, long merchantId) {
		MemMerchant memMerchant = merchantService.getMerchantById(merchantId).getMemMerchant();
		if(type == 0){
			//加黑名单
			memMerchant.setDelFlag(1);
		}else {
			//解除黑名单
			memMerchant.setDelFlag(0);
		}
		memMerchant.setUpdated(DateUtils.currentTime());
		memMerchantMapper.update(memMerchant);
		redisOperate.hmset(memMerchant.redisKey(), memMerchant);
		return Result.jsonSuccess();
	}

	/**
	 * 获取公告列表
	 * @param page
	 * @param pagesize
	 * @param title
	 * @return
	 */
	public String getBannerList(int page, int pagesize, String title) {
		Map<String, String> mapKV = new HashMap<String, String>();
		mapKV.put("startIndex", String.valueOf((page-1)*pagesize));
		mapKV.put("pageSize", String.valueOf(pagesize));
		mapKV.put("title", String.valueOf(title));

		List<CmsBanner> list = cmsBannerMapper.getAllBannerList(mapKV);
		int count = cmsBannerMapper.getAllBannerListCount(title);
		return Result.jsonSuccess(new Pager<>(count, list));
	}

	public String deleteBanner(long id) {
		cmsBannerMapper.delete(id);
		return Result.jsonSuccess();
	}

	public String publishBanner(long id) {
		CmsBanner banner = cmsBannerMapper.getBannerById(id);
		if (banner != null) {
			banner.setUpdated(DateUtils.currentTime());
			if (banner.getIspublished() == 0) {
				banner.setIspublished(1);
			} else {
				banner.setIspublished(0);
			}
			cmsBannerMapper.update(banner);
		}else{
			throw IllegalConstException.errorException(JiLuParams.ID);
		}
		return Result.jsonSuccess();
	}
	
	/**
	 * 获取公告 fansd
	 * 
	 * @return
	 */
	public String getBanner(long id) {
		CmsBanner banner = cmsBannerMapper.getBannerById(id);
		AssumeRoleForm form = fileuploadService.assumeRole(0);
		try {
			banner.setFmUrl(aliyunService.getUrl(form, banner.getFmUrl().substring(banner.getFmUrl().indexOf("common/"))));
			if(banner.getGdType() == 1)
				banner.setGdUrl(aliyunService.getUrl(form, banner.getGdUrl().substring(banner.getGdUrl().indexOf("common/"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.jsonSuccess(banner);
	}

	/**
	 * 吉币报表
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String getJbCzLog(int page, int pageSize,int startTime,int endTime) {
		Map<String, Object> mapKV = new HashMap<String, Object>();
		mapKV.put("start", (page-1)*pageSize);
		mapKV.put("pageSize", pageSize);
		mapKV.put("startTime", startTime);
		mapKV.put("endTime", endTime);

		long count = memPayInfoMapper.getJbCzLogCount(mapKV);
		if(count == 0)
			return Result.jsonSuccess(Pager.EMPTY);
		List<MemPayInfo> list = memPayInfoMapper.getJbCzLog(mapKV);
		List<CzLogForm> list2 = new ArrayList<>();
		for(MemPayInfo memPayInfo : list){
			list2.add(new CzLogForm(memPayInfo));
		}
		return Result.jsonSuccess(new Pager<>(count, list2));
	}
	
}
