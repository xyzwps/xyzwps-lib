package com.xyzwps.lang.simple.statement;

import com.xyzwps.lang.simple.*;
import com.xyzwps.lang.simple.expression.value.Bool;

import static com.xyzwps.lang.simple.ReducedResult.*;
import static com.xyzwps.lang.simple.EvaluatedResult.*;

public record While(Expression condition, Statement body) implements Statement {

    @Override
    public EvaluatedResult evaluate(Environment env) {
        var evalCond = condition.evaluate(env);
        if (evalCond instanceof EvalValue condValue) {
            if (condValue.value() instanceof Bool cond) {
                if (cond.value()) {
                    var evalBody = body.evaluate(env);
                    if (evalBody instanceof EvalEnviroment bodyEnv) {
                        return evaluate(bodyEnv.environment());
                    } else {
                        throw new IllegalStateException();
                    }
                } else {
                    return new EvalEnviroment(env);
                }
            } else {
                throw new IllegalStateException();
            }
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean reducible() {
        return true;
    }

    @Override
    public ReducedResult reduce(Environment env) {
        return new ReducedStatement(
                new If(condition, new Sequence(body, this), new DoNothing()),
                env
        );
    }

    @Override
    public String toString() {
        return String.format("while (%s) { %s }", condition, body);
    }
}
