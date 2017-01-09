package com.busap.vcs.chat.service.kafka.encoder;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

import com.busap.vcs.base.WsMessage;
import com.busap.vcs.chat.util.SerializeUtil;


public class KafkaEncoder implements Encoder<WsMessage> {
	public KafkaEncoder(VerifiableProperties prop){
		
	}
	public byte[] toBytes(WsMessage msg) {
		return SerializeUtil.ObjectToByte(msg);
	}

}
