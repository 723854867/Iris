package org.Iris.aliyun.bean;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;

public enum SignatureMethod {

	HMAC_SHA1("HMAC-SHA1") {
		@Override
		public String encode(String source, String key) {
			byte[] buffer = HmacUtils.hmacSha1(key, source);
			return new Base64().encodeToString(buffer);
		}
	};
	
	private String mark;
	private SignatureMethod(String mark) {
		this.mark = mark;
	}
	public String mark() {
		return mark;
	}
	
	public abstract String encode(String source, String key);
}
