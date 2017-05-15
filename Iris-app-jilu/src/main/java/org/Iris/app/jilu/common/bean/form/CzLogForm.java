package org.Iris.app.jilu.common.bean.form;

import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.storage.domain.MemPayInfo;
import org.Iris.util.lang.DateUtils;

public class CzLogForm {

	private String merchantName;
	private int totalAmount;
	private int totalJb;
	private String czTime;
	
	public CzLogForm(MemPayInfo memPayInfo){
		this.totalAmount = memPayInfo.getTotalAmount();
		this.totalJb = memPayInfo.getTotalJb();
		this.czTime = DateUtils.getUTCDate((long)memPayInfo.getCzTime()*1000);
		this.merchantName = Beans.merchantService.getMerchantById(memPayInfo.getMerchantId()).getMemMerchant().getName();
	}
	
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public int getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	public int getTotalJb() {
		return totalJb;
	}
	public void setTotalJb(int totalJb) {
		this.totalJb = totalJb;
	}
	public String getCzTime() {
		return czTime;
	}
	public void setCzTime(String czTime) {
		this.czTime = czTime;
	}
	
	
	
}
