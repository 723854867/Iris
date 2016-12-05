package org.Iris.dispatcher.event;

public interface IEventListener {
	
	/**
	 * 表示只处理该类型的 {@link Event}
	 * 
	 * @return
	 */
	EventType eventType();
	
	/**
	 * 处理接收到的事件
	 * 
	 * @param event
	 */
	void onEvent(IEvent event);
}
