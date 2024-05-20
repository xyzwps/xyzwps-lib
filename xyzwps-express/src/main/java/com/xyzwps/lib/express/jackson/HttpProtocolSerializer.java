package com.xyzwps.lib.express.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.xyzwps.lib.express.HttpProtocol;

import java.io.IOException;

public class HttpProtocolSerializer extends StdSerializer<HttpProtocol> {

    public HttpProtocolSerializer() {
        super(HttpProtocol.class);
    }

    @Override
    public void serialize(HttpProtocol httpProtocol, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        if (httpProtocol == null) {
            json.writeNull();
            return;
        }

        json.writeString(httpProtocol.value);
    }
}
