package com.xyzwps.lang.simple.expression;

import com.xyzwps.lang.simple.Environment;
import com.xyzwps.lang.simple.Expression;
import com.xyzwps.lang.simple.ReducedResult;
import com.xyzwps.lang.simple.expression.value.Num;

import static com.xyzwps.lang.simple.ReducedResult.*;

public record Add(Expression left, Expression right) implements Expression {
    @Override
    public boolean reducible() {
        return true;
    }

    @Override
    public ReducedResult reduce(Environment env) {
        if (left.reducible()) {
            var reducedLeft = left.reduce(env);
            if (reducedLeft instanceof ReducedExpression expression) {
                return new ReducedExpression(new Add(expression.expression(), right));
            } else {
                throw new IllegalStateException();
            }
        } else if (right.reducible()) {
            var reducedRight = right.reduce(env);
            if (reducedRight instanceof ReducedExpression expression) {
                return new ReducedExpression(new Add(left, expression.expression()));
            } else {
                throw new IllegalStateException();
            }
        } else {
            if (left instanceof Num ln && right instanceof Num rn) {
                return new ReducedExpression(new Num(ln.value() + rn.value()));
            } else {
                throw new IllegalStateException();
            }
        }
    }

    @Override
    public String toString() {
        return left + " + " + right;
    }
}
