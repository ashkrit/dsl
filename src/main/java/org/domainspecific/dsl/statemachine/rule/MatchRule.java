package org.domainspecific.dsl.statemachine.rule;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class MatchRule<State, Event, Message> {
    public final State preState;
    public final Event currentEvent;
    public final Consumer<Message> processor;
    public final Optional<State> nextState;
    public final Optional<State> failedState;

    public MatchRule(State preState, Event currentEvent, Consumer<Message> processor, Optional<State> nextState, Optional<State> failedState) {
        this.preState = preState;
        this.currentEvent = currentEvent;
        this.processor = processor;
        this.nextState = nextState;
        this.failedState = failedState;
    }


    @Override
    public String toString() {
        return String.format("{ preState:%s , currentEvent:%s , nextState:%s, failedState:%s }", preState, currentEvent,
                nextState.map(Objects::toString).orElse("NA"),
                failedState.map(Objects::toString).orElse("NA"));
    }
}
