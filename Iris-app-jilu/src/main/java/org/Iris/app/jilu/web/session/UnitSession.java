package org.Iris.app.jilu.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.service.realm.unit.UnitAdapter;
import org.Iris.core.exception.IrisRuntimeException;

public class UnitSession<T extends UnitAdapter<?>> extends IrisSession {
	
	protected T unit;

	public UnitSession(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public T getUnit() {
		return unit;
	}
	
	public void setUnit(T unit) {
		if (null != this.unit)
			throw new IrisRuntimeException("Unit Session already bind to another unit!");
		this.unit = unit;	
	}
}
