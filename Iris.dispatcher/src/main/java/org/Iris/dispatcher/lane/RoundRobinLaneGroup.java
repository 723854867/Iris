package org.Iris.dispatcher.lane;

import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLaneGroup extends LaneGroup {
	
	private AtomicInteger idx = new AtomicInteger(0);

	public RoundRobinLaneGroup(int laneNum) {
		super(laneNum);
	}

	@Override
	public Lane getLane() {
		return lanes[Math.abs(idx.getAndIncrement() % lanes.length)];
	}
	
	@Override
	public void returnLane(Lane lane) {
		// do nothing
	}
}
