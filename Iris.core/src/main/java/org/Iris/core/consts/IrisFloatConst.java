package org.Iris.core.consts;

public class IrisFloatConst extends IrisConstImpl<Float>{

	public IrisFloatConst(String key, float value) {
		super(key, value);
	}

	public IrisFloatConst(String key, float value, int constId) {
		super(key, value, constId);
	}
	
	@Override
	protected Float parseValue(String value) {
		// TODO Auto-generated method stub
		return Float.valueOf(value);
	}

}
