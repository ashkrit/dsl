package org.domainspecific.dsl.bdd;

import java.util.EmptyStackException;
import java.util.Stack;

import static org.domainspecific.dsl.bdd.Specification.specification;

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


        });

    }
}
