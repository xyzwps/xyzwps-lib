package com.xyzwps.lib.json.element;

public sealed interface JsonElement
        permits JsonArray, JsonBoolean, JsonDecimal, JsonInteger, JsonNull, JsonObject, JsonString {

    <R> R acceptVisitor(JsonElementVisitor<R> visitor);

    default Object toJavaObject() {
        return this.acceptVisitor(ToJavaObjectVisitor.INSTANCE);
    }

    default String toPrettyString() {
        return this.acceptVisitor(new ToPrettyStringVisitor()).toString();
    }

    String toString();
}
