package com.xyzwps.lang.simple;

import com.xyzwps.lang.simple.expression.Value;

public interface EvaluatedResult {

    record EvalValue(Value value) implements EvaluatedResult {
    }

    record EvalEnviroment(Environment environment) implements EvaluatedResult {
    }
}
