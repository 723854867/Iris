package com.busap.vcs.data.enums;


/**
 * 礼物type/礼物用途
 * @author huoshanwei
 *
 */
public enum GiftPurposeEnum {

	normal(1,"收视榜"),
	enlarge(2,"人气榜"),
	snow(3,"直播"),
	fireworks(4,"其它");

	private Integer code;

	private String name;

	GiftPurposeEnum(Integer code, String name) {
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
	public static GiftPurposeEnum valueByCode(Integer code) {
		for (GiftPurposeEnum type : GiftPurposeEnum.values()) {
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		return null;
	}

}
