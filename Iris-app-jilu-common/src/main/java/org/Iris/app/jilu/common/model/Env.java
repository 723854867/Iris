package org.Iris.app.jilu.common.model;

public enum Env {

	TEST,
	LOCAL,
	ONLINE;
	
	public static final Env match(String env) {
		for (Env temp : Env.values()) {
			if (temp.name().equalsIgnoreCase(env))
				return temp;
		}
		return null;
	}
}
