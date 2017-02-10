package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.redis.RedisHashBean;
import org.Iris.util.lang.DateUtils;
/**
 * 个推客户端注册id
 * @author 樊水东
 * 2017年2月10日
 */
public class MemCid implements RedisHashBean{

	private long merchantId;
	private String clientId;
	private int type;
	private int created;
	private int updated;
	
	public MemCid(){
		
	}
	
	public MemCid(long merchantId,String clientId,int type){
		this.merchantId = merchantId;
		this.clientId = clientId;
		this.type = type;
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
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
		return MerchantKeyGenerator.merchantCIDDataKey(merchantId);
	}
	
	
}
