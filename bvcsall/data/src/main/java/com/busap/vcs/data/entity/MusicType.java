package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//用户投诉
@Entity
@Table(name = "music_type")
public class MusicType extends BaseEntity{  
	 
	private static final long serialVersionUID = 5528045754200890148L;
	
	@Column(length=100)
	private String typeName; //音乐类型

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
}
