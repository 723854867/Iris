package com.busap.vcs.serializer;

import java.io.IOException;

import com.busap.vcs.data.entity.Label;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LabelJsonSerializer extends JsonSerializer<Label> {

	@Override
	public void serialize(Label value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeStartObject();  
		jgen.writeStringField("name",value.getName());
        jgen.writeNumberField("id", value.getId());
        jgen.writeNumberField("num", value.getNum() == null ?0:value.getNum());
		jgen.writeNumberField("createDate", value.getCreateDate() == null ?0:value.getCreateDate());
        jgen.writeEndObject();
	}

}
