package org.Iris.dispatcher.event;

import java.util.concurrent.atomic.AtomicReference;

import org.Iris.core.exception.IrisRuntimeException;
import org.Iris.dispatcher.SerialEventDispatcher;

public abstract class SerialEventListener implements IEventListener {

	private AtomicReference<SerialEventDispatcher> holder = new AtomicReference<SerialEventDispatcher>();
	
	public void bind(SerialEventDispatcher dispatcher) {
		if (!holder.compareAndSet(null, dispatcher))
			throw new IrisRuntimeException("SerialEventListener has bind to another SerialEventDispatcher!");
	}
	
	public void unbind(SerialEventDispatcher dispatcher) {
		if (!holder.compareAndSet(dispatcher, null))
			throw new IrisRuntimeException("SerialEventListener bind state changed!");
	}
}
