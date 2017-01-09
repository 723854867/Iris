package com.busap.vcs.service.kafka.encoder;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

import com.busap.vcs.base.WsMessage;
import com.busap.vcs.util.BeanUtils;


public class WsKafkaEncoder implements Encoder<WsMessage> {
	public WsKafkaEncoder(VerifiableProperties prop){
		
	}
	public byte[] toBytes(WsMessage msg) {
		return BeanUtils.obj2Bytes(msg);
	}

}
