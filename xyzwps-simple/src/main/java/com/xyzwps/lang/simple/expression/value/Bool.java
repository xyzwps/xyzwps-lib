package com.xyzwps.lang.simple.expression.value;

import com.xyzwps.lang.simple.expression.Value;

public record Bool(boolean value) implements Value {
    @Override
    public String toString() {
        return value + "";
    }
}
