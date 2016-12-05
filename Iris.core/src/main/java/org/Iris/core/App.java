package org.Iris.core;

import org.Iris.core.service.locale.Locale;

public abstract class App {
	
	protected String name;
	
	private Locale locale = Locale.DEFAULT_LANG;

	protected App(String name) {
		this.name = name;
	}
	
	public final void start() {
		locale.init();
		this.bootstrap();
	}
	
	protected abstract void bootstrap();
	
	public abstract void stop();
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
