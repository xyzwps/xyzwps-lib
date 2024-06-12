package com.xyzwps.lib.json.element;

import java.math.BigInteger;

public record JsonInteger(BigInteger value) implements JsonElement {

    public JsonInteger(int value) {
        this(BigInteger.valueOf(value));
    }

    @Override
    public String toString() {
        return this.acceptVisitor(ToJsonStringVisitor.INSTANCE);
    }

    @Override
    public <R> R acceptVisitor(JsonElementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
