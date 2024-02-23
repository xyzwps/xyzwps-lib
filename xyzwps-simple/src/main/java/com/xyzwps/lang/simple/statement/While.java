package com.xyzwps.lang.simple.statement;

import com.xyzwps.lang.simple.Environment;
import com.xyzwps.lang.simple.Expression;
import com.xyzwps.lang.simple.ReducedResult;
import com.xyzwps.lang.simple.Statement;

import static com.xyzwps.lang.simple.ReducedResult.*;

public record While(Expression condition, Statement body) implements Statement {

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
