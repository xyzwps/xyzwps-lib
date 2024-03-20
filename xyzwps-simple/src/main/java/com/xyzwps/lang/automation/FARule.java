package com.xyzwps.lang.automation;

import java.util.Objects;

public record FARule(int state, Character character, int nextState) {
    public boolean canApplyTo(int state, Character character) {
        return this.state == state && Objects.equals(this.character, character);
    }

    public int follow() {
        return nextState;
    }

    @Override
    public String toString() {
        return String.format("FARule %d --%s--> %d", state, character, nextState);
    }
}
