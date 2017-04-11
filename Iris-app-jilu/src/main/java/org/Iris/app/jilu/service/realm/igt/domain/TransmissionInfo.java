package org.Iris.app.jilu.service.realm.igt.domain;

import org.Iris.app.jilu.common.bean.enums.IgtPushType;

public class TransmissionInfo {

	private PushCommonParam param;
	private int type;

	public TransmissionInfo(PushCommonParam param, IgtPushType type) {
		super();
		this.param = param;
		this.type = type.type();
	}

	public PushCommonParam getParam() {
		return param;
	}

	public void setParam(PushCommonParam param) {
		this.param = param;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
