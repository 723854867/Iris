package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "template")
//活动模板表
public class Template extends BaseEntity{  
	 
	private static final long serialVersionUID = 2935854139327750559L;
	
	private String  title;//模板名称
	
	private String  pic;//图片url
	
	private String  backgroundPic;//模板背景图片url
	
	private String  description;//zip包描述
	
	private int  orderNum;//zip包排序
	
	private Long  actId;//模板属于哪个活动
	
	private String  zipUrl;//模板文件路径
	
	private int  type=0;//类型 0 模板，1滤镜 ,2照片电影,3 MV
	
	private String versionNum;//照片电影版本号

	
	public String getBackgroundPic() {
		return backgroundPic;
	}

	public void setBackgroundPic(String backgroundPic) {
		this.backgroundPic = backgroundPic;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public Long getActId() {
		return actId;
	}

	public void setActId(Long actId) {
		this.actId = actId;
	}

	public String getZipUrl() {
		return zipUrl;
	}

	public void setZipUrl(String zipUrl) {
		this.zipUrl = zipUrl;
	}

	public String getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}
	 
	
}
