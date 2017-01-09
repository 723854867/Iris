package com.busap.vcs.data.enums;


/**
 * 组织类型
 * @author zx
 *
 */
public enum OrganizationTypeEnum {
	
	

	rebate(1,"返点"),
	salary(2,"底薪");
	
	private Integer value;
	
	private String description;
	
	
	private OrganizationTypeEnum(Integer value,String description){
		this.value=value;
		this.description=description;
	}


	public Integer getValue() {
		return value;
	}


	public void setValue(Integer value) {
		this.value = value;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
