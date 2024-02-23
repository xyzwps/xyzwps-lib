package com.xyzwps.lang.simple.statement;

import com.xyzwps.lang.simple.Environment;
import com.xyzwps.lang.simple.Expression;
import com.xyzwps.lang.simple.ReducedResult;
import com.xyzwps.lang.simple.Statement;
import com.xyzwps.lang.simple.expression.Value;

import static com.xyzwps.lang.simple.ReducedResult.*;

public record Assign(String name, Expression exp) implements Statement {
    @Override
    public boolean reducible() {
        return true;
    }

    @Override
    public ReducedResult reduce(Environment env) {
        if (exp().reducible()) {
            var reduced = exp.reduce(env);
            if (reduced instanceof ReducedExpression expression) {
                return new ReducedStatement(
                        new Assign(name, expression.expression()),
                        env
                );
            } else {
                throw new IllegalStateException();
            }
        } else {
            if (exp instanceof Value value) {
                return new ReducedStatement(
                        new DoNothing(),
                        env.add(name, value)
                );
            } else {
                throw new IllegalStateException();
            }
        }
    }

    @Override
    public String toString() {
        return name + " = " + exp;
    }
}
