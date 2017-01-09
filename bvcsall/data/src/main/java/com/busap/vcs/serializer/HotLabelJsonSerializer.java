package com.busap.vcs.serializer;

import java.io.IOException;

import com.busap.vcs.data.entity.HotLabel;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class HotLabelJsonSerializer extends JsonSerializer<HotLabel> {

	@Override
	public void serialize(HotLabel value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeStartObject();  
		jgen.writeStringField("name",value.getLabelName());
        jgen.writeNumberField("id", value.getId());  
        jgen.writeEndObject();
	}

}
