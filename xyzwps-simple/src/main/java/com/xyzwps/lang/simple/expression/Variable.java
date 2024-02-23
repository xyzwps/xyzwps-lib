package com.xyzwps.lang.simple.expression;

import com.xyzwps.lang.simple.Environment;
import com.xyzwps.lang.simple.Expression;
import com.xyzwps.lang.simple.ReducedResult;

import static com.xyzwps.lang.simple.ReducedResult.*;

public record Variable(String name) implements Expression {
    @Override
    public boolean reducible() {
        return true;
    }

    @Override
    public ReducedResult reduce(Environment env) {
        var value = env.get(name);
        if (value == null) {
            throw new RuntimeException("com.xyzwps.lang.simple.expression.Variable not defined");
        }
        return new ReducedExpression(value);
    }

    @Override
    public String toString() {
        return name;
    }
}
