package com.xyzwps.lang.simple;

import com.xyzwps.lang.simple.expression.Add;
import com.xyzwps.lang.simple.expression.LessThan;
import com.xyzwps.lang.simple.expression.Multiply;
import com.xyzwps.lang.simple.expression.Variable;
import com.xyzwps.lang.simple.expression.value.Num;
import com.xyzwps.lang.simple.statement.*;


public class Main {

    public static void main(String[] args) {

        var statement = new Sequence(
                new Sequence(
                        new Assign("x", new Num(1)),
                        new Assign("y", new Num(1))
                ),
                new While(
                        new LessThan(new Variable("x"), new Num(5)),
                        new Sequence(
                                new Assign("x", new Add(new Variable("x"), new Num(1))),
                                new Assign("y", new Multiply(new Variable("y"), new Variable("x")))
                        )
                )
        );

        new Machine(statement, new Environment()).run();

        System.out.println(statement.evaluate(new Environment()));
    }
}
