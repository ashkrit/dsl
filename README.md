# dsl
Domain Specific Language - Contains example of internal DSL.

## State Machine DSL - Circuit Breaker
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

### Decision Table  - FX Trade 
```
RuleEngine<FXTransaction> ds = decisionSystem("FX Transaction", tradeSchema, s -> {

            s.rule("High Transfer discount", condition -> {
                condition
                        .gt("amount", 1000d)
                        .eq("source", "SGD")
                        .eq("target", "INR")
                        .action((rule, row) -> row.setDiscount(2.0d));
            });

            s.rule("less than minimum", condition -> {
                condition
                        .lt("amount", 1000d)
                        .action((rule, row) -> row.setDiscount(-1.0d));
            });


            s.rule("limited time period", conditions -> {
                        conditions
                                .between("date", 20200805, 20200831)
                                .action((rule, row) -> row.setDiscount(5.0d));
                    }
            );

        });
```