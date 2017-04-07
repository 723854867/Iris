package org.Iris.app.jilu.service.realm.igt.domain;

import org.Iris.app.jilu.common.bean.enums.IgtPushType;

public class TransmissionInfo {

	private PushCommonParam param;
	private IgtPushType type;

	public TransmissionInfo(PushCommonParam param, IgtPushType type) {
		super();
		this.param = param;
		this.type = type;
	}

	public PushCommonParam getParam() {
		return param;
	}

	public void setParam(PushCommonParam param) {
		this.param = param;
	}

	public IgtPushType getType() {
		return type;
	}

	public void setType(IgtPushType type) {
		this.type = type;
	}

}
