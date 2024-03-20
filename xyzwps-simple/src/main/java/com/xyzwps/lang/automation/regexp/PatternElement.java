package com.xyzwps.lang.automation.regexp;

import com.xyzwps.lang.automation.NFADesign;

public interface PatternElement {

    default String bracket(int outerPrecedence) {
        if (precedence() < outerPrecedence) {
            return '(' + toString() + ')';
        } else {
            return toString();
        }
    }

    int precedence();

    NFADesign toNFADesign();

    default boolean matches(String string) {
        return toNFADesign().accept(string);
    }
}
