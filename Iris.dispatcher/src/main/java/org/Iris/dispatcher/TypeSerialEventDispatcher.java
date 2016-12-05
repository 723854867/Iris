package org.Iris.dispatcher;

import java.util.HashMap;
import java.util.Map;

import org.Iris.dispatcher.event.EventType;
import org.Iris.dispatcher.event.IEvent;
import org.Iris.dispatcher.event.SerialEventListener;
import org.Iris.dispatcher.lane.LaneGroup;
import org.jetlang.core.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * EventType 相同的 IEventListener 视为同一个实例
 * 
 * 注意：
 * 该类型的 Dispatcher 只能注册 SerialEventListener，并且一个 listener 只能绑定一个 dispatcher，因此
 * SerialEventListener.onEvent() 内部是线程安全的不需要考虑并发问题
 * </pre>
 * 
 * @author ahab
 */
public class TypeSerialEventDispatcher extends SerialEventDispatcher {
	
	private static final Logger logger = LoggerFactory.getLogger(TypeSerialEventDispatcher.class);
	
	private SerialEventListener any;
	private Map<EventType, SerialEventListener> typeBasedListeners;
	private Map<SerialEventListener, Disposable> listenersDisposables;

	public TypeSerialEventDispatcher(LaneGroup laneGroup) {
		super(laneGroup);
		this.listenersDisposables = new HashMap<SerialEventListener, Disposable>();
	}

	@Override
	protected void onRegister(SerialEventListener listener) {
		if (listener.eventType() == EventType.ANY) {
			if (null != any) {
				_disposeListener(any);
				logger.info("SerialEventListener replace: {} ---> {}", listener, any);
			}
			any = listener;
		} else {
			if (null == typeBasedListeners)
				typeBasedListeners = new HashMap<EventType, SerialEventListener>();
			SerialEventListener old = typeBasedListeners.put(listener.eventType(), listener);
			if (null != old) {
				_disposeListener(old);
				logger.info("SerialEventListener replace: {} ---> {}", listener, old);
			}
		}
		listenersDisposables.put(listener, doRegister(listener));
	}

	@Override
	protected void onDeregister(SerialEventListener listener) {
		if (listener.eventType() == listener.eventType()) {
			if (null != any) {
				_disposeListener(any);
				logger.info("SerialEventListener removed: {}", any);
				any = null;
			}
		} else {
			listener = null == typeBasedListeners ? null : typeBasedListeners.remove(listener.eventType());
			if (null != listener) {
				_disposeListener(listener);
				logger.info("SerialEventListener removed: {}", any);
			}
		}
	}
	
	@Override
	protected void publishEventDirect(IEvent event) {
		if (null != any)
			any.onEvent(event);
		SerialEventListener listener = null != typeBasedListeners ? typeBasedListeners.get(event.eventType()) : null;
		if (null != listener)
			listener.onEvent(event);
	}
	
	private void _disposeListener(SerialEventListener listener) {
		Disposable disposable = listenersDisposables.remove(listener);
		disposable.dispose();
	}

	@Override
	protected void onDispose() {
		any = null;
		typeBasedListeners.clear();
		for (Disposable disposable : listenersDisposables.values())
			disposable.dispose();
		listenersDisposables.clear();
	}
}
