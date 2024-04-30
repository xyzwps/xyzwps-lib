package com.xyzwps.lib.express.server.craft;

import java.util.Objects;

public record HeaderLine(String name, String value) {
    public HeaderLine {
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return name + ": " + value;
    }
}