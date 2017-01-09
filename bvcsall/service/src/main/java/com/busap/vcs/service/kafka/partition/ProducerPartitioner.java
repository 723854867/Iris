package com.busap.vcs.service.kafka.partition;



import com.busap.vcs.base.Message;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class ProducerPartitioner implements Partitioner<Message> {
	
	public ProducerPartitioner(VerifiableProperties p){
		
	}
	public int partition(Message key, int numPartitions) {
		int part = Math.abs(key.hashCode())%numPartitions;
		return part;
	}

}
