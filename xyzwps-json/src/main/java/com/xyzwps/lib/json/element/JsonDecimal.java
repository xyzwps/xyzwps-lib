package com.xyzwps.lib.json.element;

import java.math.BigDecimal;

public record JsonDecimal(BigDecimal value) implements JsonElement {

    @Override
    public String toString() {
        return this.acceptVisitor(ToJsonStringVisitor.INSTANCE);
    }

    @Override
    public <R> R acceptVisitor(JsonElementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
