package com.busap.vcs.data.enums;


/**
 * 任务大类型
 * @author zx
 *
 */
public enum TaskTypeOneEnum {
	
	

	daily(1,"日常任务"),
	limit(2,"限时任务"),
	once(3,"一次性任务"),
	special(4,"特殊任务");
	
	private Integer value;
	
	private String description;
	
	
	private TaskTypeOneEnum(Integer value,String description){
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
