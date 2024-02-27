package com.xyzwps.lang.automation;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NFARuleBook {
    private final List<FARule> rules;

    public NFARuleBook(FARule... rules) {
        this.rules = Arrays.asList(rules);
    }

    public Set<Integer> nextStates(Set<Integer> states, char character) {
        return states.stream()
                .flatMap(state -> followRulesFor(state, character).stream())
                .collect(Collectors.toSet());
    }

    public List<FARule> rulesFor(int state, char character) {
        return rules.stream().filter(it -> it.canApplyTo(state, character)).toList();
    }

    public Set<Integer> followRulesFor(int state, char character) {
        return rulesFor(state, character).stream().map(FARule::follow).collect(Collectors.toSet());
    }
}
