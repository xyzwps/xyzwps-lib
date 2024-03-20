package com.xyzwps.lang.automation.regexp;

import com.xyzwps.lang.automation.FARule;
import com.xyzwps.lang.automation.NFADesign;
import com.xyzwps.lang.automation.NFARuleBook;

import java.util.Set;

public record Literal(char character) implements PatternElement {

    /**
     * <pre>
     *                c
     * ----> ( 0 ) -------> ((1))
     * </pre>
     */
    @Override
    public NFADesign toNFADesign() {
        int startState = 0;
        var acceptStates = Set.of(1);
        var rules = new NFARuleBook(
                new FARule(startState, character, 1)
        );
        return new NFADesign(startState, acceptStates, rules);
    }

    @Override
    public int precedence() {
        return 3;
    }

    @Override
    public String toString() {
        return "" + character;
    }
}