package com.xyzwps.lib.express.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.xyzwps.lib.express.HttpSearchParams;

import java.io.IOException;

public class HttpSearchParamsSerializer extends StdSerializer<HttpSearchParams> {

    public HttpSearchParamsSerializer() {
        super(HttpSearchParams.class);
    }


    @Override
    public void serialize(HttpSearchParams httpSearchParams, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        if (httpSearchParams == null) {
            json.writeNull();
            return;
        }

        json.writeStartObject();
        for (var name : httpSearchParams.names()) {
            var values = httpSearchParams.getAll(name);
            if (values == null || values.isEmpty()) {
                json.writeObjectField(name, null);
            } else if (values.size() == 1) {
                json.writeObjectField(name, values.getFirst());
            } else {
                json.writeObjectField(name, values);
            }
        }
        json.writeEndObject();
    }
}
