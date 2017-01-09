package com.busap.vcs.data.enums;


/**
 * 用户状态
 * @author dmsong
 *
 */
public enum TimeTypeEnum {

	day(0,"24小时"),
	week(1,"一周"),
	month(2,"一月"),
	year(3,"一年");

	private Integer code;

	private String name;

	TimeTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// 获得 enum 对象
	public static TimeTypeEnum valueByCode(Integer code) {
		for (TimeTypeEnum type : TimeTypeEnum.values()) {
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		return null;
	}

}
