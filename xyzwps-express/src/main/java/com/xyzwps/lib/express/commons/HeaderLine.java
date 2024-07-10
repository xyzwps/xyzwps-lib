package com.xyzwps.lib.express.commons;

import java.util.Objects;

public record HeaderLine(String name, String value) {
    public HeaderLine {
        Objects.requireNonNull(name, "Invalid header line: no name");
        Objects.requireNonNull(value, "Invalid header line: no value");
    }

    @Override
    public String toString() {
        return String.format("%s: %s", name, value);
    }
}