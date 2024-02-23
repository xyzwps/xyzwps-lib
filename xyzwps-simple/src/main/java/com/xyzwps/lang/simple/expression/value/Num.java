package com.xyzwps.lang.simple.expression.value;

import com.xyzwps.lang.simple.Environment;
import com.xyzwps.lang.simple.EvaluatedResult;
import com.xyzwps.lang.simple.expression.Value;

import static com.xyzwps.lang.simple.EvaluatedResult.*;

public record Num(double value) implements Value {

    @Override
    public EvaluatedResult evaluate(Environment env) {
        return new EvalValue(this);
    }

    @Override
    public String toString() {
        return value + "";
    }
}
