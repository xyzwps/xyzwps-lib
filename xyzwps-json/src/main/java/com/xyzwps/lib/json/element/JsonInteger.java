package com.xyzwps.lib.json.element;

import java.math.BigInteger;

public record JsonInteger(BigInteger value) implements JsonElement {

    @Override
    public String toString() {
        return value.toString();
    }
}
