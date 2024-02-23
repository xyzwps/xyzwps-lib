package com.xyzwps.lang.simple.expression.value;

import com.xyzwps.lang.simple.expression.Value;

public record Num(double value) implements Value {
    @Override
    public String toString() {
        return value + "";
    }
}
