package org.domainspecific.dsl;

import org.domainspecific.dsl.statemachine.StateMachine;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateMachineTest {


    @Test
    public void circuit_breaker_remains_open_when_call_is_successful() {
        Consumer<String> noOp = s -> {
        };

        StateMachine<String, String, String> cbMachine = StateMachine.create("Circuit Breaker", m -> {
            m.startWith("OPEN");

            m.at("OPEN", rule -> {

                rule.when("connect", noOp, "OPEN", "CLOSED");

            });


        });


        cbMachine.process("connect", "https://my.superservice.com");
        assertEquals("OPEN", cbMachine.getCurrentState());

    }


}
