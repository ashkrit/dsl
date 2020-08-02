package org.domainspecific.dsl.statemachine;

import org.domainspecific.dsl.statemachine.StateMachine;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.domainspecific.dsl.statemachine.StateMachine.machine;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateMachineTest {


    @Test
    public void circuit_breaker_remains_open_when_call_is_successful() {
        Consumer<String> noOp = s -> { };

        StateMachine<String, String, String> cbMachine = machine("Circuit Breaker", m -> {
            m.startWith("OPEN");

            m.at("OPEN", rule ->
                    rule.when("connect", noOp, "OPEN", "CLOSED"));


        });


        cbMachine.process("connect", "https://my.superservice.com");
        assertEquals("OPEN", cbMachine.currentState());
    }


    @Test
    public void circuit_breaker_close_when_call_is_not_successful() {
        Consumer<String> failOp = s -> {
            throw new RuntimeException("Something went wrong");
        };


        StateMachine<String, String, String> cbMachine = machine("Circuit Breaker", m -> {
            m.startWith("OPEN");

            m.at("OPEN", rule -> {
                rule.when("connect", failOp, "OPEN", "CLOSED");
            });

            m.at("CLOSED", rule -> {
                rule.when("connect", s -> {
                }, "CLOSED");
            });

        });


        IntStream.range(0, 5)
                .forEach($ -> cbMachine.process("connect", "https://my.superservice.com"));

        assertEquals("CLOSED", cbMachine.currentState());

    }

    @Test
    public void circuit_breaker_half_open_when_1_call_successful() {
        Consumer<String> failOp = s -> {
            throw new RuntimeException("Something went wrong");
        };


        StateMachine<String, String, String> cbMachine = machine("Circuit Breaker", machine -> {
            machine.startWith("OPEN");

            machine.at("OPEN", rule -> {
                rule.when("connect", failOp, "OPEN", "CLOSED");
            });

            machine.at("CLOSED", rule -> {
                rule.when("connect", s -> { }, "CLOSED");
                rule.when("re-connect", s -> { }, "HALF-OPEN");
            });

            machine.at("HALF-OPEN", rule -> {
                rule.when("connect", s -> { }, "CLOSED");
                rule.when("re-open", s -> { }, "OPEN");

            });

        });


        cbMachine.process("connect", "https://my.superservice.com");
        cbMachine.process("re-connect", "https://my.superservice.com");

        assertEquals("HALF-OPEN", cbMachine.currentState());

        cbMachine.process("re-open", "https://my.superservice.com");
        assertEquals("OPEN", cbMachine.currentState());

    }


}
