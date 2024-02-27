package com.xyzwps.lang.automation;

public record FARule(int state, char character, int nextState) {
    public boolean canApplyTo(int state, char character) {
        return this.state == state && this.character == character;
    }

    public int follow() {
        return nextState;
    }

    @Override
    public String toString() {
        return String.format("FARule %d --%s--> %d", state, character, nextState);
    }
}
