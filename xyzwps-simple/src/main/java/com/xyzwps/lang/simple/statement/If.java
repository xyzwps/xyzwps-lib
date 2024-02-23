package com.xyzwps.lang.simple.statement;

import com.xyzwps.lang.simple.*;
import com.xyzwps.lang.simple.expression.value.Bool;

import static com.xyzwps.lang.simple.ReducedResult.*;
import static com.xyzwps.lang.simple.EvaluatedResult.*;

public record If(Expression condition,
                 Statement consequence,
                 Statement alternative
) implements Statement {

    @Override
    public EvaluatedResult evaluate(Environment env) {
        var evalCond = condition.evaluate(env);
        if (evalCond instanceof EvalValue evalValue) {
            if (evalValue.value() instanceof Bool bool) {
                return bool.value()
                        ? consequence.evaluate(env)
                        : alternative.evaluate(env);
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public boolean reducible() {
        return true;
    }

    @Override
    public ReducedResult reduce(Environment env) {
        if (condition.reducible()) {
            var reduced = condition.reduce(env);
            if (reduced instanceof ReducedExpression expression) {
                return new ReducedStatement(
                        new If(expression.expression(), consequence, alternative),
                        env
                );
            } else {
                throw new IllegalStateException();
            }
        } else {
            if (condition instanceof Bool bool) {
                if (bool.value()) {
                    return new ReducedStatement(consequence, env);
                } else {
                    return new ReducedStatement(alternative, env);
                }
            } else {
                throw new IllegalStateException();
            }
        }
    }

    @Override
    public String toString() {
        return String.format("if (%s) { %s } else { %s }", condition, consequence, alternative);
    }
}
