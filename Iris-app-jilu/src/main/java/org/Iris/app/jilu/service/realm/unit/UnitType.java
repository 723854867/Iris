package org.Iris.app.jilu.service.realm.unit;

import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;

/**
 * 单位类型
 * 
 * @author Ahab
 */
public enum UnitType {

	/**
	 * 商户
	 * 
	 */
	MERCHANT(0),
	
	/**
	 * 客户
	 * 
	 */
	CUSTOMER(1);
	
	private int mark;
	private UnitType(int mark) {
		this.mark = mark;
	}
	public int mark() {
		return mark;
	}
	
	public String getUnitLock(long uid) { 
		return RedisKeyGenerator.getUnitLockKey(this, uid);
	}
}
