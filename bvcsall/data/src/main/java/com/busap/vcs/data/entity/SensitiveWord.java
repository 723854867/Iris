package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

//敏感词表
@Entity
@Table(name = "sensitive_word")
public class SensitiveWord extends BaseEntity{  
	 
	private static final long serialVersionUID = -7218297974655843153L;

	private String  word; 
	
	private Integer  status;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
