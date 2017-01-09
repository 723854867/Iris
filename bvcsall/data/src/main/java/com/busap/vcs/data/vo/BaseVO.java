package com.busap.vcs.data.vo;

import java.util.Date;

public class BaseVO {
	
	 private Long id;		//实体id
	 
	 private Date createDate;//创建时间
	 
	 private Date create_at;//创建时间
	 
	 private Date modifyDate;//修改时间

	 private Long creatorId; //创建人id 
	    
	 private String dataFrom;//数据来源

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public Date getCreate_at() {
		return create_at;
	}

	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
		this.createDate = create_at;
	}
	 
	 
}
