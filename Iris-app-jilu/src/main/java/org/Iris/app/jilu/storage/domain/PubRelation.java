package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.redis.RedisHashBean;

public class PubRelation implements RedisHashBean {

	private String id;
	private long applier;
	private long acceptor;
	private String applierMemo;
	private String acceptorMemo;
	private int mod;
	private int created;
	private int updated;
	
	public PubRelation() {}
	
	public PubRelation(String relationId) {
		this.id = relationId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getApplier() {
		return applier;
	}

	public void setApplier(long applier) {
		this.applier = applier;
	}

	public long getAcceptor() {
		return acceptor;
	}

	public void setAcceptor(long acceptor) {
		this.acceptor = acceptor;
	}

	public String getApplierMemo() {
		return applierMemo;
	}

	public void setApplierMemo(String applierMemo) {
		this.applierMemo = applierMemo;
	}

	public String getAcceptorMemo() {
		return acceptorMemo;
	}

	public void setAcceptorMemo(String acceptorMemo) {
		this.acceptorMemo = acceptorMemo;
	}
	
	public int getMod() {
		return mod;
	}
	
	public void setMod(int mod) {
		this.mod = mod;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public int getUpdated() {
		return updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}
	
	@Override
	public String redisKey() {
		return CommonKeyGenerator.relationDataKey(this.id);
	}
}
