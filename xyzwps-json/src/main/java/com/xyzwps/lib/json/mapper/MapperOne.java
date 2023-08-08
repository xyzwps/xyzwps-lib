package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.*;

import java.util.Objects;

class MapperOne<T> {

    private final Class<T> tClass;

    private Mapper<T, JsonNull> withNull;
    private Mapper<T, JsonArray> withArray;
    private Mapper<T, JsonBoolean> withBoolean;
    private Mapper<T, JsonDecimal> withDecimal;
    private Mapper<T, JsonInteger> withInteger;
    private Mapper<T, JsonObject> withObject;
    private Mapper<T, JsonString> withString;

    Class<T> getValueClass() {
        return tClass;
    }

    MapperOne(Class<T> tClass) {
        this.tClass = Objects.requireNonNull(tClass);
    }

    void withNull(Mapper<T, JsonNull> it) {
        this.withNull = Objects.requireNonNull(it);
    }

    void withArray(Mapper<T, JsonArray> it) {
        this.withArray = Objects.requireNonNull(it);
    }

    void withBoolean(Mapper<T, JsonBoolean> it) {
        this.withBoolean = Objects.requireNonNull(it);
    }

    void withDecimal(Mapper<T, JsonDecimal> it) {
        this.withDecimal = Objects.requireNonNull(it);
    }

    void withInteger(Mapper<T, JsonInteger> it) {
        this.withInteger = Objects.requireNonNull(it);
    }

    void withObject(Mapper<T, JsonObject> it) {
        this.withObject = Objects.requireNonNull(it);
    }

    void withString(Mapper<T, JsonString> it) {
        this.withString = Objects.requireNonNull(it);
    }

    Mapper<T, JsonArray> getForArray() {
        return this.withArray;
    }

    Mapper<T, JsonBoolean> getForBoolean() {
        return this.withBoolean;
    }

    Mapper<T, JsonDecimal> getForDecimal() {
        return this.withDecimal;
    }

    Mapper<T, JsonInteger> getForInteger() {
        return this.withInteger;
    }

    Mapper<T, JsonNull> getForNull() {
        return this.withNull;
    }

    Mapper<T, JsonObject> getForObject() {
        return this.withObject;
    }

    Mapper<T, JsonString> getForString() {
        return this.withString;
    }
}
