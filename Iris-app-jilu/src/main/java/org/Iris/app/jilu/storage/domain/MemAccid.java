package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.service.realm.wyyx.result.WyyxCreateAccountResultForm;
import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.redis.RedisHashBean;
import org.Iris.util.lang.DateUtils;
/**
 * 网易云信账号实体
 * @author 樊水东
 * 2017年2月10日
 */
public class MemAccid implements RedisHashBean{

	private long merchantId;
	private String accid;
	private String token;
	private int created;
	private int updated;
	
	public MemAccid(){
		
	}
	
	public MemAccid(long merchantId,String accid,String token){
		this.merchantId = merchantId;
		this.accid = accid;
		this.token = token;
		int time = DateUtils.currentTime();
		this.created = time;
		this.updated = time;
	}
	
	public MemAccid(long merchantId,WyyxCreateAccountResultForm result){
		this.merchantId = merchantId;
		this.accid = result.getInfo().getAccid();
		this.token = result.getInfo().getToken();
		int time = DateUtils.currentTime();
		this.created = time;
		this.updated = time;
	}
	
	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

	public String getAccid() {
		return accid;
	}

	public void setAccid(String accid) {
		this.accid = accid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public int getUpdated() {
		return updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}

	@Override
	public String redisKey() {
		return MerchantKeyGenerator.merchantACCIDDataKey(merchantId);
	}
	
	
}
