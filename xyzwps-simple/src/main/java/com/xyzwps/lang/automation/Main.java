package com.xyzwps.lang.automation;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        var book = new DFARuleBook(
                new FARule(1, 'a', 2), new FARule(1, 'b', 1),
                new FARule(2, 'a', 2), new FARule(2, 'b', 3),
                new FARule(3, 'a', 3), new FARule(3, 'b', 3)
        );

        var dfaDesign = new DFADesign(1, Set.of(3), book);

        System.out.println(dfaDesign.canAccept("baaab"));
        System.out.println(dfaDesign.canAccept("baaa"));
    }
}
