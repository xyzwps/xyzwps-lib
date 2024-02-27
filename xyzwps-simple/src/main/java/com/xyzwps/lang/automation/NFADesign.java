package com.xyzwps.lang.automation;

import java.util.Set;

public class NFADesign {
    private final int startState;
    private final Set<Integer> acceptStates;
    private final NFARuleBook ruleBook;

    public NFADesign(int startState, Set<Integer> acceptStates, NFARuleBook ruleBook) {
        this.startState = startState;
        this.acceptStates = acceptStates;
        this.ruleBook = ruleBook;
    }


    public NFA toNFA() {
        return new NFA(startState, acceptStates, ruleBook);
    }

    public boolean accept(String string) {
        return toNFA().readString(string).accepting();
    }

}
