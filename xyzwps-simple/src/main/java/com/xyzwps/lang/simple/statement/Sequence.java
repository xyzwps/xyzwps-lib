package com.xyzwps.lang.simple.statement;

import com.xyzwps.lang.simple.Environment;
import com.xyzwps.lang.simple.ReducedResult;
import com.xyzwps.lang.simple.Statement;

import static com.xyzwps.lang.simple.ReducedResult.*;

public record Sequence(Statement first, Statement second) implements Statement {
    @Override
    public boolean reducible() {
        return true;
    }

    @Override
    public ReducedResult reduce(Environment env) {
        if (first instanceof DoNothing) {
            return new ReducedStatement(second, env);
        } else {
            if (first.reducible()) {
                var reducedFirst = first.reduce(env);
                if (reducedFirst instanceof ReducedStatement statement) {
                    return new ReducedStatement(
                            new Sequence(statement.statement(), second),
                            statement.environment()
                    );
                } else {
                    throw new IllegalStateException();
                }
            } else {
                throw new IllegalStateException("Cannot reduce statement " + first.getClass().getCanonicalName());
            }
        }
    }

    @Override
    public String toString() {
        return first + "; " + second;
    }
}
