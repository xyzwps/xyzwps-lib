package com.xyzwps.lang.automation.regexp;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Concatenate(PatternElement first, PatternElement second) implements PatternElement {
    @Override
    public int precedence() {
        return 1;
    }

    @Override
    public String toString() {
        return Stream.of(first, second)
                .map(pattern -> pattern.bracket(precedence()))
                .collect(Collectors.joining());
    }
}
