package org.Iris.dispatcher.event;

public interface IEvent {

	EventType eventType();
	
	Object attach();
}
