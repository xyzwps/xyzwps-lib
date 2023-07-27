package com.xyzwps.lib.json;

import com.xyzwps.lib.json.util.CharGenerator;

import java.util.Objects;

public class StringCharGenerator implements CharGenerator {

    private final String str;
    private final int length;
    private int current = 0;

    public StringCharGenerator(String str) {
        this.str = Objects.requireNonNull(str);
        this.length = str.length();
    }

    @Override
    public boolean hasNext() {
        return current < length;
    }

    @Override
    public char next() {
        return str.charAt(current++);
    }

    @Override
    public char seek() {
        return str.charAt(current);
    }
}
