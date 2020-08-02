package org.domainspecific.dsl.statemachine.rule;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class MatchRules<State, Event, Message> {
    private final List<MatchRule> rules;
    private final State preState;

    public MatchRules(List<MatchRule> rules, State preState) {
        this.preState = preState;
        this.rules = rules;
    }

    public void when(Event currentEvent, Consumer<Message> processor, State nextState, State failed) {
        rules.add(new MatchRule<>(preState, currentEvent, processor, Optional.ofNullable(nextState), Optional.ofNullable(failed)));
    }

    public void when(Event currentEvent, Consumer<Message> processor, State nextState) {
        when(currentEvent, processor, nextState, null);
    }
}
