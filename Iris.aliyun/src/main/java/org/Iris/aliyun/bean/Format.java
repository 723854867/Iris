package org.Iris.aliyun.bean;

import org.Iris.util.common.SerializeUtil;

public enum Format {

	JSON {
		public <T> T strToBean(String content, Class<T> clazz) {
			return SerializeUtil.JsonUtil.GSON.fromJson(content, clazz);
		}
	},
	
	XML {
		public <T> T strToBean(String content, Class<T> clazz) {
			return SerializeUtil.XmlUtil.xmlToBean(content, clazz);
		}
	};
	
	public abstract <T> T strToBean(String content, Class<T> clazz);
}
