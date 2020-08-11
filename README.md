# dsl
Domain Specific Language - Contains example of internal DSL.

Blog post - https://ashkrit.blogspot.com/2020/08/speak-language-of-domain.html explain about internal DSL

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

### Testing framework
```
public class ExampleSpec {
    {
        specification("A Stack", scenario -> {

            scenario.should("Empty stack should have size 0", then -> {
                Stack<String> stack = new Stack<>();

                then.value(stack.size()).shouldBe(0);
            });

            scenario.should("pop values in last-in-first-out order", then -> {
                Stack<String> stack = new Stack<>();
                stack.push("1");
                stack.push("2");

                then.value(stack.pop()).shouldBe("2");
                then.value(stack.pop()).shouldBe("1");
            });

            scenario.should("throw NoSuchElementException if an empty stack is popped", then -> {
                Stack<String> stack = new Stack<>();
                then.shouldThrow(EmptyStackException.class, () -> stack.pop());
            });

            scenario.should("Size is equal to values pushed", then -> {
                Stack<String> stack = new Stack<>();
                stack.push("1");
                stack.push("2");

                then.value(stack.size()).shouldBe(3);
            });

            scenario.should("Simulate unhandled case", then -> {
                Stack<String> stack = null;

                then.value(stack.size()).shouldBe(3);
            });


        });

    }
}
```

### Trading System
```
 newOrder(equity("IBM"), (order, ts) -> {
            order
                    .buy(100)
                    .at(126.07d);

            ts.execute(order);
        });

        newOrder(equity("GOOG"), (order, ts) -> {
            order
                    .sell(100)
                    .at(1506.85)
                    .partial();

            ts.execute(order);
        });
```

