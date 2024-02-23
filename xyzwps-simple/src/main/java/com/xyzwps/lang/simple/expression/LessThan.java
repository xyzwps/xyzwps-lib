package com.xyzwps.lang.simple.expression;

import com.xyzwps.lang.simple.Environment;
import com.xyzwps.lang.simple.EvaluatedResult;
import com.xyzwps.lang.simple.Expression;
import com.xyzwps.lang.simple.ReducedResult;
import com.xyzwps.lang.simple.expression.value.Bool;
import com.xyzwps.lang.simple.expression.value.Num;

import static com.xyzwps.lang.simple.ReducedResult.*;
import static com.xyzwps.lang.simple.EvaluatedResult.*;

public record LessThan(Expression left, Expression right) implements Expression {
    @Override
    public EvaluatedResult evaluate(Environment env) {
        var evalLeft = left.evaluate(env);
        var evalRight = right.evaluate(env);
        if (evalLeft instanceof EvalValue lval && evalRight instanceof EvalValue rval) {
            if (lval.value() instanceof Num ln && rval.value() instanceof Num rn) {
                return new EvalValue(new Bool(ln.value() < rn.value()));
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
        if (left.reducible()) {
            var reducedLeft = left.reduce(env);
            if (reducedLeft instanceof ReducedExpression expression) {
                return new ReducedExpression(new LessThan(expression.expression(), right));
            } else {
                throw new IllegalStateException();
            }
        } else if (right.reducible()) {
            var reducedRight = right.reduce(env);
            if (reducedRight instanceof ReducedExpression expression) {
                return new ReducedExpression(new LessThan(left, expression.expression()));
            } else {
                throw new IllegalStateException();
            }
        } else {
            if (left instanceof Num ln && right instanceof Num rn) {
                return new ReducedExpression(new Bool(ln.value() < rn.value()));
            } else {
                throw new IllegalStateException();
            }
        }
    }

    @Override
    public String toString() {
        return left + " < " + right;
    }
}
