package com.xyzwps.lang.automation;

import java.util.Set;

public class NFA {
    private Set<Integer> currentStates;
    private final Set<Integer> acceptStates;
    private final NFARuleBook ruleBook;

    public NFA(int startState, Set<Integer> acceptStates, NFARuleBook ruleBook) {
        this.currentStates = Set.of(startState);
        this.acceptStates = acceptStates;
        this.ruleBook = ruleBook;
    }

    public boolean accepting() {
        return acceptStates.stream().anyMatch(currentStates::contains);
    }

    public void readCharacter(char character) {
        this.currentStates = ruleBook.nextStates(currentStates, character);
    }

    public NFA readString(String string) {
        for (var character : string.toCharArray()) {
            readCharacter(character);
        }
        return this;
    }
}
