package com.xyzwps.lib.json.element;

import java.math.BigDecimal;

public record JsonDecimal(BigDecimal value) implements JsonElement {

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Object toJavaObject() {
        return value;
    }
}
