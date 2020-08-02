package org.domainspecific.dsl.statemachine;

public interface MachineEvent<State, Event, Message> {
    void apply(StateMachine<State, Event, Message> sm);
}
