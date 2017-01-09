package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.busap.vcs.serializer.HotLabelJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@Entity
@Table(name = "hot_label")
@JsonSerialize(using = HotLabelJsonSerializer.class)  
public class HotLabel extends BaseEntity{  
	 
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5269569115414852355L;

	private long labelId; 
	
	private String labelName;
	
	@Column(columnDefinition="int not null DEFAULT '0'")
	private int  displayOrder;
	
	@Column(columnDefinition="int not null DEFAULT '0'")
	private int showOrder;

	@Column(name = "share_img",columnDefinition = "varchar(500)  NULL ", nullable=true)
	private String shareImg;

	@Column(name = "share_text",columnDefinition = "varchar(500)  NULL ", nullable=true)
	private String shareText;

	public String getShareImg() {
		return shareImg;
	}

	public void setShareImg(String shareImg) {
		this.shareImg = shareImg;
	}

	public String getShareText() {
		return shareText;
	}

	public void setShareText(String shareText) {
		this.shareText = shareText;
	}

	public long getLabelId() {
		return labelId;
	}

	public void setLabelId(long labelId) {
		this.labelId = labelId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public int getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}

	
	
	
	
	
	
}
