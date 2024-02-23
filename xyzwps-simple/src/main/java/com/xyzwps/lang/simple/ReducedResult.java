package com.xyzwps.lang.simple;

public sealed interface ReducedResult {

    record ReducedExpression(Expression expression) implements ReducedResult {
        @Override
        public String toString() {
            return expression().toString();
        }
    }

    record ReducedStatement(Statement statement, Environment environment) implements ReducedResult {
        @Override
        public String toString() {
            return String.format("%s %s", statement, environment);
        }
    }
}
