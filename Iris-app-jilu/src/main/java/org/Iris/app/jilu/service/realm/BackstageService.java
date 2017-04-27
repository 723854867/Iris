package org.Iris.app.jilu.service.realm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.bean.form.LabelApplyForm;
import org.Iris.app.jilu.common.bean.form.Pager;
import org.Iris.app.jilu.storage.domain.BgUser;
import org.Iris.app.jilu.storage.domain.BgVersion;
import org.Iris.app.jilu.storage.domain.BuyLabelLog;
import org.Iris.app.jilu.storage.domain.MemLabelBind;
import org.Iris.app.jilu.storage.domain.MemMerchant;
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
	public String versionGet() {
		return Result.jsonSuccess(bgVersionMapper.getVersions());
	}

	/**
	 * 添加版本
	 */
	public String addVersion(String versionNum, int status) {
		BgVersion bgVersion = new BgVersion(versionNum, status);
		bgVersionMapper.insert(bgVersion);
		redisOperate.del(bgVersion.redisKey());
		return Result.jsonSuccess();
	}

	/**
	 * 版本修改
	 */
	public String updateVersion(long versionId, String versionNum, int status){
		BgVersion bgVersion = new BgVersion(versionId, versionNum, status);
		if(versionId==redisOperate.hgetAll(CommonKeyGenerator.getVersion(),new BgVersion()).getVersionId())
			redisOperate.del(bgVersion.redisKey());
		bgVersionMapper.update(bgVersion);
		return Result.jsonSuccess();
	}
	/**
	 * 版本删除
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public String delVersion(long versionId){
		if(versionId==redisOperate.hgetAll(CommonKeyGenerator.getVersion(), new BgVersion()).getVersionId())
			redisOperate.del(CommonKeyGenerator.getVersion());
		bgVersionMapper.delete(DateUtils.currentTime(), versionId);
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
}
