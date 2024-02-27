package com.xyzwps.lang.automation;

import java.util.Set;

public class DFADesign {
    private int currentState;
    private final Set<Integer> acceptStates;
    private final DFARuleBook ruleBook;

    public DFADesign(int currentState, Set<Integer> acceptStates, DFARuleBook ruleBook) {
        this.currentState = currentState;
        this.acceptStates = acceptStates;
        this.ruleBook = ruleBook;
    }

    public DFA toDFA() {
        return new DFA(currentState, acceptStates, ruleBook);
    }

    public boolean canAccept(String string) {
        return toDFA().readString(string).canAccept();
    }

}
