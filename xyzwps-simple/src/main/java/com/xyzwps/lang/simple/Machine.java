package com.xyzwps.lang.simple;

import static com.xyzwps.lang.simple.ReducedResult.*;

public class Machine {
    Reducible reducible;
    Environment env;

    Machine(Reducible reducible, Environment env) {
        this.reducible = reducible;
        this.env = env;
    }

    void step() {
        switch (reducible.reduce(env)) {
            case ReducedExpression expression -> {
                this.reducible = expression.expression();
            }
            case ReducedStatement statement -> {
                this.reducible = statement.statement();
                this.env = statement.environment();
            }
        }
    }

    void run() {
        while (reducible.reducible()) {
            System.out.printf("%s %s\n", reducible, env);
            step();
        }
        System.out.printf("%s %s\n", reducible, env);
    }
}
