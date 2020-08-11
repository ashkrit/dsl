package org.domainspecific.dsl.bdd;

import java.util.function.Supplier;

public class Matchers {

    private final String name;
    private final SpecificationResult specification;

    public Matchers(String name, SpecificationResult specification) {
        this.name = name;
        this.specification = specification;
    }


    public Conditions value(Object value) {
        return new Conditions(value);
    }

    public void shouldThrow(Class<? extends Exception> expectedException, Supplier<Object> s) {
        try {
            s.get();
            raiseTestFailure("No Exception thrown");
        } catch (Exception e) {
            if (notSame(expectedException, e)) {
                String format = String.format("Expected %s but found %s", expectedException, e.getClass());
                raiseTestFailure(format);
            }
        }
    }

    private boolean notSame(Class<? extends Exception> exception, Exception e) {
        return !(e.getClass() == exception);
    }

    private void raiseTestFailure(String message) {
        throw new AssertionError(message);
    }
}
