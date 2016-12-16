package org.Iris.aliyun.policy;

public class Statement {

	private String Effect;
	private Object Action;
	private Object Resource;
	
	public Statement() {}
	
	public Statement(Effect effect) {
		this.Effect = effect.name();
	}
	
	public void setEffect(Effect effect) {
		Effect = effect.name();
	}
	
	public String getEffect() {
		return Effect;
	}
	
	public void setAction(Action action) {
		Action = action.mark();
	}
	
	public Object getAction() {
		return Action;
	}
	
	public Object getResource() {
		return Resource;
	}
	
	public void setResource(Object resource) {
		Resource = resource;
	}
}
