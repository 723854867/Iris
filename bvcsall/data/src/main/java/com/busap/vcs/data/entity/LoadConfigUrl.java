package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="load_config_url")
public class LoadConfigUrl extends BaseEntity {
	private static final long serialVersionUID = 2886293741065041132L;
	 @Column(name = "status")
	private Integer status;//是否有效
	 @Column(name = "clientpf")
	private String clientPf;//客户端平台
	 @Column(name = "url")
	private String url;//加载视频的地址
	 @Column(name = "weight")
	private Integer weight;//走这个客户端的比例
	 @Column(name = "modify_name")
	private String modifyName;//修改人名称
	 @Column(name = "description")
	private String description;//邀请描述信息
	 @Column(name = "type")
	private String type;//url类型
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getClientPf() {
		return clientPf;
	}
	public void setClientPf(String clientPf) {
		this.clientPf = clientPf;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getModifyName() {
		return modifyName;
	}
	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	 
	 
}
