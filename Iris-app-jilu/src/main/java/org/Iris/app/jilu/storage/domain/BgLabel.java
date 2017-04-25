package org.Iris.app.jilu.storage.domain;

import org.Iris.util.lang.DateUtils;

/**
 * 标签
 * @author 樊水东
 * 2017年4月25日
 */
public class BgLabel {

	private long id;
	private String labelNum;
	private int price;
	private int created;
	private int updated;
	
	public BgLabel() {
		super();
	}
	public BgLabel(String labelNum, int price) {
		super();
		int time = DateUtils.currentTime();
		this.labelNum = labelNum;
		this.price = price;
		this.created = time;
		this.updated = time;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLabelNum() {
		return labelNum;
	}
	public void setLabelNum(String labelNum) {
		this.labelNum = labelNum;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
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
	
	
}
