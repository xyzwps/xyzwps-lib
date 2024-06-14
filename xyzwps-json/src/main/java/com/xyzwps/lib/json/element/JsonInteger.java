package com.xyzwps.lib.json.element;

import java.math.BigInteger;

public record JsonInteger(BigInteger value) implements JsonElement {

    public JsonInteger(int value) {
        this(BigInteger.valueOf(value));
    }

    @Override
    public String toString() {
        return this.stringify();
    }

    @Override
    public <R> R visit(JsonElementVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <R, C> R visit(C ctx, JsonElementVisitor2<R, C> visitor) {
        return visitor.visit(ctx, this);
    }
}
