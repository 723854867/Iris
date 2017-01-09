package com.busap.vcs.utils.mipush;

import com.xiaomi.xmpush.server.Constants;

/**
 * 只为初始化
 * @author yinhb
 *
 */
public abstract class MsgSender {
	static {
		//生产,android测试时候要用这个，ios测试时候用下面的，生产环境全用这个
		Constants.useOfficial();
		//测试，小米只支持ios
		//Constants.useSandbox();
	}
}
