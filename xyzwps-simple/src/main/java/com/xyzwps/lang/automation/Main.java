package com.xyzwps.lang.automation;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        dfa();
        nfa();
    }

    /**
     * <pre>
     *
     *    +---+               +---+               +---+
     *   b|   |              a|   |            a,b|   |
     *    |   V        a      |   V        b      |   V
     *    ( 1 )  -----------> ( 2 )  -----------> ((3))
     *
     * </pre>
     */
    public static void dfa() {
        System.out.println("\n\n====== DFA ======");

        var book = new DFARuleBook(
                new FARule(1, 'a', 2), new FARule(1, 'b', 1),
                new FARule(2, 'a', 2), new FARule(2, 'b', 3),
                new FARule(3, 'a', 3), new FARule(3, 'b', 3)
        );

        var dfaDesign = new DFADesign(1, Set.of(3), book);

        System.out.println(dfaDesign.canAccept("baaab"));
        System.out.println(dfaDesign.canAccept("baaa"));
    }

    /**
     * <pre>
     *
     *      +---+
     *   a,b|   |
     *      |   V        b                 a,b                 a,b
     *      ( 1 )  -----------> ( 2 )  -----------> ( 3 )  -----------> ((4))
     *
     * </pre>
     */
    public static void nfa() {
        System.out.println("\n\n====== DFA ======");

        var book = new NFARuleBook(
                new FARule(1, 'a', 1), new FARule(1, 'b', 1), new FARule(1, 'b', 2),
                new FARule(2, 'a', 3), new FARule(2, 'b', 3),
                new FARule(3, 'a', 4), new FARule(3, 'b', 4)
        );

        var nfaDesign = new NFADesign(1, Set.of(4), book);
        System.out.println(nfaDesign.accept("bab"));
        System.out.println(nfaDesign.accept("bbbbb"));
        System.out.println(nfaDesign.accept("bbabb"));
    }
}
