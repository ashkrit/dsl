# dsl
Domain Specific Language - Contains example of internal DSL.

## State Machine DSl - Circuit Breaker
```
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
```
