package org.domainspecific.dsl.statemachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class StateMachine<State, Event, Message> {
    private final String machineName;
    private final List<MatchRule> rules = new ArrayList<>();
    private State currentState;


    public StateMachine(String machineName) {
        this.machineName = machineName;
    }

    public static <State, Event, Message> StateMachine<State, Event, Message> create(String machineName, MachineEvent<State, Event, Message> me) {
        StateMachine<State, Event, Message> machine = new StateMachine<>(machineName);
        me.apply(machine);
        return machine;
    }

    public void at(State currentState, MachineRule<State, Event, Message> rule) {
        MatchRules<State, Event, Message> r = new MatchRules<>(this.rules, currentState);
        rule.apply(r);
    }

    public State getCurrentState() {
        return currentState;
    }

    private void setState(State newState) {
        this.currentState = newState;
    }


    public void startWith(State initialState) {
        this.currentState = initialState;
    }

    public void process(Event event, Message message) {
        State stateToMatch = this.currentState;
        rules.stream()
                .filter(rule -> rule.preState.equals(stateToMatch))
                .filter(rule -> rule.currentEvent.equals(event))
                .forEach(rule -> processRule(message, rule));
    }

    private void processRule(Message message, MatchRule rule) {
        try {
            rule.processor.accept(message);
            rule.nextState.ifPresent(s -> this.currentState = (State) s);
        } catch (Exception e) {
            rule.failedState.ifPresent(s -> this.currentState = (State) s);
        }
    }


    public static class MatchRules<State, Event, Message> {
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
}
