package com.xyzwps.lang.automation;

import java.util.Set;

public class DFA {
    private int currentState;
    private final Set<Integer> acceptStates;
    private final DFARuleBook ruleBook;

    public DFA(int currentState, Set<Integer> acceptStates, DFARuleBook ruleBook) {
        this.currentState = currentState;
        this.acceptStates = acceptStates;
        this.ruleBook = ruleBook;
    }

    public boolean canAccept() {
        return acceptStates.contains(currentState);
    }

    public DFA readCharacter(char character) {
        this.currentState = ruleBook.nextState(this.currentState, character);
        return this;
    }

    public DFA readString(String string) {
        for (var character : string.toCharArray()) {
            this.readCharacter(character);
        }
        return this;
    }
}
