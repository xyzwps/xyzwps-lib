package com.xyzwps.lang.automation;

import java.util.Set;

public class NFA {
    private Set<Integer> _currentStates;
    private final Set<Integer> acceptStates;
    private final NFARuleBook ruleBook;

    public NFA(int startState, Set<Integer> acceptStates, NFARuleBook ruleBook) {
        this._currentStates = Set.of(startState);
        this.acceptStates = acceptStates;
        this.ruleBook = ruleBook;
    }

    private Set<Integer> getCurrentStates() {
        this._currentStates = ruleBook.followFreeMoves(_currentStates);
        return _currentStates;
    }

    public boolean accepting() {
        return acceptStates.stream().anyMatch(getCurrentStates()::contains);
    }

    public void readCharacter(char character) {
        this._currentStates = ruleBook.nextStates(this.getCurrentStates(), character);
    }

    public NFA readString(String string) {
        for (var character : string.toCharArray()) {
            readCharacter(character);
        }
        return this;
    }
}
