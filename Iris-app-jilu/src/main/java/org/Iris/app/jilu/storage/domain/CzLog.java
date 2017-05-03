package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.bean.enums.CzMoneyType;
import org.Iris.util.lang.DateUtils;

/**
 * 用户充值
 * @author 樊水东
 * 2017年5月3日
 */
public class CzLog {

	private long id;
	private String czId;
	private long merchantId;
	private int czTime;
	private String product;
	private int money;
	private int jb;
	private int created;
	
	public CzLog() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CzLog(String czId, long merchantId, int czTime, String product) {
		super();
		this.czId = czId;
		this.merchantId = merchantId;
		this.czTime = czTime;
		this.product = product;
		int type = Integer.valueOf(product.substring(product.length()-1));
		this.jb = CzMoneyType.getMoney(type);
		this.money = jb/10;
		this.created = DateUtils.currentTime();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCzId() {
		return czId;
	}
	public void setCzId(String czId) {
		this.czId = czId;
	}
	public long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	public int getCzTime() {
		return czTime;
	}
	public void setCzTime(int czTime) {
		this.czTime = czTime;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getJb() {
		return jb;
	}
	public void setJb(int jb) {
		this.jb = jb;
	}
	public int getCreated() {
		return created;
	}
	public void setCreated(int created) {
		this.created = created;
	}
	
	
	
}
