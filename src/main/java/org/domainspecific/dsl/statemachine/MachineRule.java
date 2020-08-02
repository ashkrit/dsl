package org.domainspecific.dsl.statemachine;

import org.domainspecific.dsl.statemachine.rule.MatchRules;

public interface MachineRule<State, Event, Message> {
    void apply(MatchRules<State, Event, Message> rule);
}
