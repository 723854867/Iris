package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

//用户投诉
@Entity
@Table(name = "music")
public class Music extends BaseEntity{  
	 
	private static final long serialVersionUID = -8090636198262001774L;

	@Column(length=100)
	private String name; //音乐名称
	
	@Column(length=20)
	private Long size; //文件大小，单位：kb
	
	private String url;
	
	private Integer typeId; //音乐类型id
	
	private Integer status = 1; //状态 1:有效，0：无效
	
	private String description;
	
	private String faceUrl;//封面url
	
	private int orderNumber = 0;//权重
	
	@Transient
	private String uploader;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public String getFaceUrl() {
		return faceUrl;
	}

	public void setFaceUrl(String faceUrl) {
		this.faceUrl = faceUrl;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	
}
