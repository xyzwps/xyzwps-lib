package com.xyzwps.lang.simple.statement;

import com.xyzwps.lang.simple.Environment;
import com.xyzwps.lang.simple.EvaluatedResult;
import com.xyzwps.lang.simple.ReducedResult;
import com.xyzwps.lang.simple.Statement;

import static com.xyzwps.lang.simple.ReducedResult.*;
import static com.xyzwps.lang.simple.EvaluatedResult.*;

public record Sequence(Statement first, Statement second) implements Statement {
    @Override
    public EvaluatedResult evaluate(Environment env) {
        if (first instanceof DoNothing) {
            return second.evaluate(env);
        } else {
            var evaluatedFirst = first.evaluate(env);
            if (evaluatedFirst instanceof EvalEnviroment evalEnv) {
                return second.evaluate(evalEnv.environment());
            }
            throw new IllegalStateException();
        }
    }

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
