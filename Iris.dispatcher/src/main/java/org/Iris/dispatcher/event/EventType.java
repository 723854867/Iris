package org.Iris.dispatcher.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.Iris.core.exception.IrisRuntimeException;

public class EventType {
	
	// only for EventListener that can process all Event
	public static final EventType ANY												= EventType.create(0x00);
	
	// EventListener register to a dispatcher
	public static final EventType LISTENER_REGISTER									= EventType.create(0x01);
	
	// EventListener deregister from a dispatcher
	public static final EventType LISTENER_DEREGISTER								= EventType.create(0x02);
	
	// Dispatcher dispose event
	public static final EventType DISPATCHER_DISPOSE								= EventType.create(0x03);

	
	private static Map<Integer, EventType> map = new ConcurrentHashMap<Integer, EventType>();
	
	private int mark;
	
	private EventType(int mark) {
		this.mark = mark;
	}
	
	public static EventType create(int type) { 
		EventType eventType = map.get(type);
		if (null != eventType)
			throw new IrisRuntimeException("EventType - " + type + " conflict, it has already defined!");
		eventType = new EventType(type);
		EventType temp = map.putIfAbsent(type, eventType);
		if (null != temp)
			throw new IrisRuntimeException("EventType - " + type + " conflict, it has already defined!");
		return eventType;
	}
	
	public int mark() {
		return mark;
	}
}
