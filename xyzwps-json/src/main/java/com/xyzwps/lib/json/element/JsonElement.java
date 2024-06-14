package com.xyzwps.lib.json.element;

public sealed interface JsonElement
        permits JsonArray, JsonBoolean, JsonDecimal, JsonInteger, JsonNull, JsonObject, JsonString {

    <R> R visit(JsonElementVisitor<R> visitor);

    <R, C> R visit(C ctx, JsonElementVisitor2<R, C> visitor);

    default Object toJavaObject() {
        return this.visit(ToJavaObjectVisitor.INSTANCE);
    }

    default String stringify(boolean pretty) {
        if (pretty) {
            return this.visit(0, new ToPrettyStringVisitor()).toString();
        } else {
            return this.visit(ToJsonStringVisitor.INSTANCE);
        }
    }

    default String stringify() {
        return this.stringify(false);
    }
}
