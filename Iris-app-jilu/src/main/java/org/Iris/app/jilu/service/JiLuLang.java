package org.Iris.app.jilu.service;

import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.core.service.locale.Locale;
import org.Iris.core.util.IrisProperties;

public class JiLuLang extends Locale {
	
	public JiLuLang(String langConfigurationLocation) {
		super(langConfigurationLocation);
	}

	@Override
	protected void init(IrisProperties langs) {
		for (JiLuCode code : JiLuCode.values()) 
			code.parse(langs.getOptional(code));
	}
}
