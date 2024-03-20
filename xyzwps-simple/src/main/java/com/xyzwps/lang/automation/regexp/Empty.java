package com.xyzwps.lang.automation.regexp;

import com.xyzwps.lang.automation.NFADesign;
import com.xyzwps.lang.automation.NFARuleBook;

import java.util.Set;

public record Empty() implements PatternElement {

    /**
     * <pre>
     *     -----> ((0))
     * </pre>
     */
    @Override
    public NFADesign toNFADesign() {
        int startState = 0;
        var acceptStats = Set.of(0);
        var book = new NFARuleBook();
        return new NFADesign(startState, acceptStats, book);
    }

    @Override
    public int precedence() {
        return 3;
    }

    @Override
    public String toString() {
        return "";
    }
}
