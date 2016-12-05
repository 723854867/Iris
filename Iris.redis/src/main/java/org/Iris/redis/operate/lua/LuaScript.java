package org.Iris.redis.operate.lua;

public class LuaScript {

	private String sha1Key;
	private String content;
	private boolean stored;
	
	public LuaScript(String sha1Key, String content) {
		this.sha1Key = sha1Key;
		this.content = content;
	}
	
	public String getSha1Key() {
		return sha1Key;
	}
	
	public String getContent() {
		return content;
	}
	
	public boolean isStored() {
		return stored;
	}
	
	public void setStored(boolean stored) {
		this.stored = stored;
	}
}
