package com.busap.vcs.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "live_notice")
// 直播预告
public class LiveNotice extends BaseEntity {

	private static final long serialVersionUID = -6183240435276108071L;
	
	private String description; //描述
	
	private String pic; //直播图片
	
	private Date showTime;  //预计直播日期
	
	private int status;  //状态，1：可用，0：不可用

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Long getShowTime() {
		return showTime.getTime();
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
