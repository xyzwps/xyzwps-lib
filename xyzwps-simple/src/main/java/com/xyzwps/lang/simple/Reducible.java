package com.xyzwps.lang.simple;

public interface Reducible {
    default boolean reducible() {
        return false;
    }

    default ReducedResult reduce(Environment env) {
        throw new RuntimeException("Cannot reduce");
    }

    EvaluatedResult evaluate(Environment env);
}
