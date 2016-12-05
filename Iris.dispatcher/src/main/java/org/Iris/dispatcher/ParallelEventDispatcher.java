package org.Iris.dispatcher;

import org.Iris.dispatcher.event.IEvent;
import org.Iris.dispatcher.event.IEventListener;
import org.Iris.dispatcher.lane.Lane;
import org.Iris.dispatcher.lane.LaneGroup;

/**
 * <pre>
 * 并行 EventDispatcher，该类型的 Dispatcher 一般是多个线程共享，因此需要处理好并发问题，一般仅作模块解耦用。
 * 
 * 注意：
 * 注册到该 Dispatcher 的 IEventListener 能被多个 Dispatcher 使用，因此其 onEvent 方法要保证线程安全。
 * </pre>
 * 
 * @author Ahab
 */
public class ParallelEventDispatcher implements IEventDispatcher<IEventListener> {
	
	private LaneGroup LaneGroup;
	
	public ParallelEventDispatcher(LaneGroup laneGroup) {
		this.LaneGroup = laneGroup;
	}

	@Override
	public void register(IEventListener eventListener) {
		
	}

	@Override
	public void deregister(IEventListener eventListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void publish(IEvent event) {
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	private class LaneDispatcher implements IEventDispatcher<IEventListener> {
		private Lane lane;
		public LaneDispatcher(LaneGroup laneGroup) {
			this.lane = laneGroup.getLane();
		}
		@Override
		public void register(IEventListener listener) {
			
		}
		@Override
		public void deregister(IEventListener listener) {
			
		}

		@Override
		public void publish(IEvent event) {
			
		}

		@Override
		public void dispose() {
			
		}
	}
}
