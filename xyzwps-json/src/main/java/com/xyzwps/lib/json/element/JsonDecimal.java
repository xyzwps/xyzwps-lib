package com.xyzwps.lib.json.element;

import java.math.BigDecimal;

public record JsonDecimal(BigDecimal value) implements JsonElement {

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
