package org.domainspecific.dsl.statemachine;

public interface MachineRule<State, Event, Message> {
    void apply(StateMachine.MatchRules<State, Event, Message> rule);
}
