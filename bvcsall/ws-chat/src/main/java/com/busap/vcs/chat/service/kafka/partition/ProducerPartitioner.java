package com.busap.vcs.chat.service.kafka.partition;



import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class ProducerPartitioner implements Partitioner {
	
	public ProducerPartitioner(VerifiableProperties p){
		
	}
	
	@Override
	public int partition(Object key, int numPartitions) {
		int part = Math.abs(key.hashCode())%numPartitions;
		return part;
	}

}
