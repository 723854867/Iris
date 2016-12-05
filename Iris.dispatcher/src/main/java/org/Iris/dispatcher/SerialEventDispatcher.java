package org.Iris.dispatcher;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.Iris.dispatcher.event.Event;
import org.Iris.dispatcher.event.EventType;
import org.Iris.dispatcher.event.IEvent;
import org.Iris.dispatcher.event.SerialEventListener;
import org.Iris.dispatcher.lane.Lane;
import org.Iris.dispatcher.lane.LaneGroup;
import org.jetlang.channels.BatchSubscriber;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.core.Callback;
import org.jetlang.core.Disposable;
import org.jetlang.core.Filter;

/**
 * 所有该 Dispatcher 中处理的事件都是串行的被执行(包括注册和注销 EventListener)，因此不存在并发问题
 * 
 * @author ahab
 *
 */
public abstract class SerialEventDispatcher implements IEventDispatcher<SerialEventListener> {
	
	protected Lane lane;
	protected LaneGroup laneGroup;
	protected MemoryChannel<IEvent> eventQueue;
	
	private boolean closed;
	protected Disposable registerDisposable;
	protected Disposable deregisterDisposable;
	protected Disposable disposeDisposable;
	
	public SerialEventDispatcher(LaneGroup laneGroup) {
		this.laneGroup = laneGroup;
		this.lane = laneGroup.getLane();
		this.eventQueue = new MemoryChannel<IEvent>();
	}
	
	protected void init() {
		registerDisposable = doRegister(new RegisterListener());
		deregisterDisposable = doRegister(new DeregisterListener());
		disposeDisposable = doRegister(new DisposeListener());
	}
	
	@Override
	public void register(SerialEventListener listener) {
		publish(new Event(EventType.LISTENER_REGISTER, listener));
	}
	
	@Override
	public void deregister(SerialEventListener listener) {
		publish(new Event(EventType.LISTENER_DEREGISTER, listener));
	}
	
	@Override
	public void publish(IEvent event) {
		if (lane.isInLane())
			publishEventDirect(event);
		else 
			eventQueue.publish(event);
	}
	
	@Override
	public void dispose() {
		publish(new Event(EventType.DISPATCHER_DISPOSE));
	}
	
	protected abstract void onRegister(SerialEventListener listener);
	
	protected abstract void onDeregister(SerialEventListener listener);
	
	protected abstract void publishEventDirect(IEvent event);
	
	protected abstract void onDispose();
	
	protected Disposable doRegister(SerialEventListener eventListener) {
		Callback<List<IEvent>> callback = createEventCallbackForListener(eventListener);

		EventType eventType = eventListener.eventType();
		if (eventType == EventType.ANY) 
			return subscribe(callback);
		Filter<IEvent> filter = new Filter<IEvent>() {
			@Override
			public boolean passes(IEvent event) {
				return (eventListener.eventType() == event.eventType());
			}
		};
		return subscribe(callback, filter);
	}
	
	protected Callback<List<IEvent>> createEventCallbackForListener(SerialEventListener eventListener) {
		Callback<List<IEvent>> eventCallback = new Callback<List<IEvent>>() {
			@Override
			public void onMessage(List<IEvent> messages) {
				for (IEvent event : messages)
					eventListener.onEvent(event);
			}
		};
		return eventCallback;
	}
	
	protected Disposable subscribe(Callback<List<IEvent>> callback) {
		BatchSubscriber<IEvent> subscriber = new BatchSubscriber<IEvent>(lane.getFiber(), callback, 0, TimeUnit.MILLISECONDS);
		return eventQueue.subscribe(subscriber);
	}

	protected Disposable subscribe(Callback<List<IEvent>> callback, Filter<IEvent> filter) {
		BatchSubscriber<IEvent> subscriber = new BatchSubscriber<IEvent>(lane.getFiber(), callback, filter, 0, TimeUnit.MILLISECONDS);
		return eventQueue.subscribe(subscriber);
	}
	
	private class RegisterListener extends SerialEventListener {
		@Override
		public EventType eventType() {
			return EventType.LISTENER_REGISTER;
		}
		@Override
		public void onEvent(IEvent event) {
			if (closed)
				return;
			bind(SerialEventDispatcher.this);
			onRegister((SerialEventListener) event.attach());
		}
	}
	
	private class DeregisterListener extends SerialEventListener {
		@Override
		public EventType eventType() {
			return EventType.LISTENER_DEREGISTER;
		}
		@Override
		public void onEvent(IEvent event) {
			if (closed)
				return;
			unbind(SerialEventDispatcher.this);
			onDeregister((SerialEventListener) event.attach());
		}
	}
	
	private class DisposeListener extends SerialEventListener {
		@Override
		public EventType eventType() {
			return EventType.DISPATCHER_DISPOSE;
		}
		@Override
		public void onEvent(IEvent event) {
			if (closed)
				return;
			
			deregisterDisposable.dispose();
			registerDisposable.dispose();
			disposeDisposable.dispose();
			deregisterDisposable = null;
			registerDisposable = null;
			disposeDisposable = null;
			onDispose();
			eventQueue.clearSubscribers();
			laneGroup.returnLane(lane);
			laneGroup = null;
			lane = null;
			closed = true;
		}
	}
}
