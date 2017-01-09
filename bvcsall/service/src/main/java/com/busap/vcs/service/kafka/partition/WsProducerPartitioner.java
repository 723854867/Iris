package com.busap.vcs.service.kafka.partition;



import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

import com.busap.vcs.base.WsMessage;

public class WsProducerPartitioner implements Partitioner<WsMessage> {
	
	public WsProducerPartitioner(VerifiableProperties p){
		
	}
	public int partition(WsMessage key, int numPartitions) {
		int part = Math.abs(key.hashCode())%numPartitions;
		return part;
	}

}
