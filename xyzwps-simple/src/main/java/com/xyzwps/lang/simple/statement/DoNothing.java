package com.xyzwps.lang.simple.statement;

import com.xyzwps.lang.simple.Environment;
import com.xyzwps.lang.simple.EvaluatedResult;
import com.xyzwps.lang.simple.Statement;

import static com.xyzwps.lang.simple.EvaluatedResult.*;

public record DoNothing() implements Statement {
    @Override
    public EvaluatedResult evaluate(Environment env) {
        return new EvalEnviroment(env);
    }

    @Override
    public String toString() {
        return "ε";
    }
}
