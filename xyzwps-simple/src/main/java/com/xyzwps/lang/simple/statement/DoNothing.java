package com.xyzwps.lang.simple.statement;

import com.xyzwps.lang.simple.Statement;

public record DoNothing() implements Statement {
    @Override
    public String toString() {
        return "ε";
    }
}
