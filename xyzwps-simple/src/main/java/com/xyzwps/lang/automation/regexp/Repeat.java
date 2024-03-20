package com.xyzwps.lang.automation.regexp;

public record Repeat(PatternElement pattern) implements PatternElement {
    @Override
    public int precedence() {
        return 2;
    }

    @Override
    public String toString() {
        return pattern.bracket(precedence()) + '*';
    }
}
