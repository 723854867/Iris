package org.Iris.dispatcher.event;

public class Event implements IEvent {

	protected EventType eventType;
	protected Object attach;
	
	public Event(EventType eventType) {
		this(eventType, null);
	}
	
	public Event(EventType eventType, Object attach) {
		this.eventType = eventType;
		this.attach = attach;
	}
	
	@Override
	public EventType eventType() {
		return this.eventType;
	}
	
	@Override
	public Object attach() {
		return attach;
	}
}
