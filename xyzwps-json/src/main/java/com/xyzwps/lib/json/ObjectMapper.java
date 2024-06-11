package com.xyzwps.lib.json;

import com.xyzwps.lib.bedrock.lang.TypeRef;
import com.xyzwps.lib.json.element.ElementParser;
import com.xyzwps.lib.json.element.SimpleParser;
import com.xyzwps.lib.json.util.StringCharGenerator;

import java.util.Objects;

public final class ObjectMapper {

    private final ElementParser elementParser;

    private final ToElement toElement;

    private final FromElement fromElement;

    public ObjectMapper() {
        this.elementParser = new SimpleParser();
        this.toElement = ToElement.createDefault();
        this.fromElement = FromElement.createDefault();
    }

    public String stringify(Object object) {
        return toElement.toElement(object).toString();
    }

    public <T> T parse(String str, Class<T> type) {
        Objects.requireNonNull(type);
        var element = elementParser.parse(new StringCharGenerator(str));
        return fromElement.fromElement(element, type);
    }

    public <T> T parse(String str, TypeRef<T> type) {
        Objects.requireNonNull(type);
        var element = elementParser.parse(new StringCharGenerator(str));
        return fromElement.fromElement(element, type.type);
    }

}
