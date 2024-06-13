package com.xyzwps.lib.json;

import com.xyzwps.lib.bedrock.lang.TypeRef;
import com.xyzwps.lib.json.element.ElementParser;
import com.xyzwps.lib.json.element.SimpleParser;
import com.xyzwps.lib.json.util.CharGenerator;

import java.io.Reader;
import java.util.Objects;
import java.util.function.Consumer;

public final class JsonMapper {

    private final ElementParser elementParser;

    private final ToElement toElement;

    private final FromElement fromElement;

    public JsonMapper() {
        this.elementParser = new SimpleParser();
        this.toElement = ToElement.createDefault();
        this.fromElement = FromElement.createDefault();
    }

    public String stringify(Object object) {
        return toElement.toElement(object).toString();
    }

    public String stringify(Object object, boolean pretty) {
        if (pretty) {
            return toElement.toElement(object).toPrettyString();
        } else {
            return this.stringify(object);
        }
    }

    public <T> T parse(Reader reader, Class<T> type) {
        Objects.requireNonNull(type);
        if (reader == null) {
            return null;
        }
        var element = elementParser.parse(CharGenerator.from(reader));
        return fromElement.fromElement(element, type);
    }

    public <T> T parse(Reader reader, TypeRef<T> type) {
        Objects.requireNonNull(type);
        if (reader == null) {
            return null;
        }
        var element = elementParser.parse(CharGenerator.from(reader));
        return fromElement.fromElement(element, type.type);
    }

    public <T> T parse(String str, Class<T> type) {
        Objects.requireNonNull(type);
        if (str == null) {
            return null;
        }
        var element = elementParser.parse(CharGenerator.from(str));
        return fromElement.fromElement(element, type);
    }

    public <T> T parse(String str, TypeRef<T> type) {
        Objects.requireNonNull(type);
        if (str == null) {
            return null;
        }
        var element = elementParser.parse(CharGenerator.from(str));
        return fromElement.fromElement(element, type.type);
    }

    public void configure(Consumer<JsonMapper> consumer) {
        consumer.accept(this);
    }

    public void addToElementConverter(Class<?> type, ToElementConverter<?> converter) {
        toElement.addToElementConverter(type, converter);
    }

    public void addToKeyConverter(Class<?> type, ToKeyConverter<?> converter) {
        toElement.addToKeyConverter(type, converter);
    }

    public void addFromElementConverter(Class<?> type, FromElementConverter<?, ?> converter) {
        fromElement.addFromElementConverter(type, converter);
    }

    public void addFromKeyConverter(Class<?> type, FromKeyConverter<?> converter) {
        fromElement.addFromKeyConverter(type, converter);
    }

}
