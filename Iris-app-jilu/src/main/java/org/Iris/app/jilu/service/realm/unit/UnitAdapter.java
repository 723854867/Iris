package org.Iris.app.jilu.service.realm.unit;

import org.Iris.app.jilu.common.Beans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnitAdapter<T extends Unit> implements Unit {
	
	private static final Logger logger = LoggerFactory.getLogger(UnitAdapter.class);
	
	protected T unit;
	private String lock;
	
	public UnitAdapter(T unit) {
		this.unit = unit;
		this.lock = unit.unitType().getUnitLock(unit.uid());
	}
	
	public String tryLock() { 
		return Beans.distributeLock.tryLock(lock);
	}
	
	public boolean unLock(String lockId) {
		boolean flag = Beans.distributeLock.unLock(lock, lockId);
		if (!flag)
			logger.warn("Unit lock {} release failure for lockId {}!", lock, lockId);
		return flag;
	}

	@Override
	public long uid() {
		return unit.uid();
	}
	
	public T getUnit() {
		return unit;
	}

	@Override
	public UnitType unitType() {
		return unit.unitType();
	}
}
