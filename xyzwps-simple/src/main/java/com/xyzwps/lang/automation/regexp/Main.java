package com.xyzwps.lang.automation.regexp;

public class Main {
    public static void main(String[] args) {
        var pattern = new Repeat(
                new Choose(
                        new Concatenate(new Literal('a'), new Literal('b')),
                        new Literal('a')
                )
        );
        System.out.println(pattern);
    }
}
