package com.xyzwps.lang.simple;

import com.xyzwps.lang.simple.expression.Value;

import java.util.HashMap;

public class Environment extends HashMap<String, Value> {

    public Environment(String name, Value value) {
        this.put(name, value);
    }

    public Environment() {
    }

    public Environment copy() {
        var newEnv = new Environment();
        newEnv.putAll(this);
        return newEnv;
    }

    public Environment add(String name, Value value) {
        var newEnv = this.copy();
        newEnv.put(name, value);
        return newEnv;
    }
}
