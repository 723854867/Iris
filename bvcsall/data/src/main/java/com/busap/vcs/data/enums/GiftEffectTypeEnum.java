package com.busap.vcs.data.enums;


/**
 * 道具特效类型
 * @author huoshanwei
 *
 */
public enum GiftEffectTypeEnum {

	normal(0,"无动画"),
	enlarge(1,"雪花"),
	snow(2,"心跳放大"),
	fireworks(3,"位移");

	private Integer code;

	private String name;

	GiftEffectTypeEnum(Integer code, String name) {
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
	public static GiftEffectTypeEnum valueByCode(Integer code) {
		for (GiftEffectTypeEnum type : GiftEffectTypeEnum.values()) {
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		return null;
	}

}
