package com.xyzwps.lib.express.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.xyzwps.lib.express.HttpPathVariables;

import java.io.IOException;

public class HttpPathVariablesSerializer extends StdSerializer<HttpPathVariables> {

    public HttpPathVariablesSerializer() {
        super(HttpPathVariables.class);
    }

    @Override
    public void serialize(HttpPathVariables httpPathVariables, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        if (httpPathVariables == null) {
            json.writeNull();
            return;
        }

        json.writeStartObject();
        for (var name : httpPathVariables.names()) {
            json.writeObjectField(name, httpPathVariables.get(name));
        }
        json.writeEndObject();

    }
}
