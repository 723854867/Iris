package org.Iris.app.jilu.common;

import org.Iris.core.consts.IrisBoolConst;
import org.Iris.core.consts.IrisIntConst;
import org.Iris.core.consts.IrisStrConst;

public interface JiLuParams {

	final IrisStrConst ACTION								= new IrisStrConst("action", 101);
	final IrisBoolConst SERIAL								= new IrisBoolConst("serial", false, 102);
	final IrisIntConst TYPE									= new IrisIntConst("type", 0, 103);
	final IrisStrConst ACCOUNT								= new IrisStrConst("account", 104);
	final IrisStrConst MOBILE								= new IrisStrConst("mobile", 105);
	final IrisStrConst EMAIL								= new IrisStrConst("email", 106);
	final IrisStrConst CAPTCHA								= new IrisStrConst("captcha", 107);
}
