package org.Iris.dispatcher.lane;

public abstract class LaneGroup {

	protected Lane[] lanes;
	
	protected LaneGroup(int laneNum) {
		lanes = new Lane[laneNum];
		for (int i = 0; i < laneNum; i++) 
			lanes[i] = new Lane(i);
	}
	
	public Lane[] lanes() {
		return lanes;
	}
	
	public abstract Lane getLane();
	
	public abstract void returnLane(Lane lane);
}
