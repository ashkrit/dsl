package org.domainspecific.dsl.main.statemachine;

import org.domainspecific.dsl.statemachine.StateMachine;

import java.util.function.Consumer;

import static org.domainspecific.dsl.statemachine.StateMachine.machine;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateMachineApp {
    public static void main(String[] args) {
        Consumer<String> failOp = s -> {
            throw new RuntimeException("Something went wrong");
        };


        StateMachine<String, String, String> cbMachine = machine("Circuit Breaker", machine -> {
            machine.startWith("OPEN");

            machine.at("OPEN", rule -> {
                rule.when("connect", failOp, "OPEN", "CLOSED");
            });

            machine.at("CLOSED", rule -> {
                rule.when("connect", s -> {
                }, "CLOSED");
                rule.when("re-connect", s -> {
                }, "HALF-OPEN");
            });

            machine.at("HALF-OPEN", rule -> {
                rule.when("connect", s -> {
                }, "CLOSED");
                rule.when("re-open", s -> {
                }, "OPEN");

            });

        });


        cbMachine.process("connect", "https://my.superservice.com");
        cbMachine.process("re-connect", "https://my.superservice.com");

        System.out.println(cbMachine.currentState());

        cbMachine.process("re-open", "https://my.superservice.com");
        System.out.println(cbMachine.currentState());

    }
}
