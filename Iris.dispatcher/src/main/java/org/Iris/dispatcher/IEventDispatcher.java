package org.Iris.dispatcher;

import org.Iris.dispatcher.event.IEvent;
import org.Iris.dispatcher.event.IEventListener;

public interface IEventDispatcher<LISTENER extends IEventListener> {
	
	void register(LISTENER listener);
	
	void deregister(LISTENER listener);

	void publish(IEvent event);
	
	void dispose();
}