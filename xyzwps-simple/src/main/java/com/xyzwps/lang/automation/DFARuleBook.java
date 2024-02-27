package com.xyzwps.lang.automation;

public class DFARuleBook {
    private final FARule[] rules;

    public DFARuleBook(FARule... rules) {
        this.rules = rules;
    }

    public int nextState(int state, char character) {
        return ruleFor(state, character).nextState();
    }

    private FARule ruleFor(int state, char character) {
        for (var rule : rules) {
            if (rule.canApplyTo(state, character)) {
                return rule;
            }
        }
        throw new IllegalStateException(String.format("No rule matched for %d --%s", state, character));
    }
}
