package com.xyzwps.lib.express.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.io.UncheckedIOException;

public class MultiValuesMapSerializer extends StdSerializer<MultiValuesMap> {

    public MultiValuesMapSerializer() {
        super(MultiValuesMap.class);
    }

    @Override
    public void serialize(MultiValuesMap it, JsonGenerator g, SerializerProvider serializerProvider) throws IOException {
        if (it == null) {
            g.writeNull();
            return;
        }

        g.writeStartObject();
        it.forEach((name, values) -> {
            try {
                if (values == null || values.isEmpty()) {
                    g.writeObjectField(name, null);
                } else if (values.size() == 1) {
                    g.writeObjectField(name, values.getFirst());
                } else {
                    g.writeObjectField(name, values);
                }

            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
        g.writeEndObject();
    }
}
