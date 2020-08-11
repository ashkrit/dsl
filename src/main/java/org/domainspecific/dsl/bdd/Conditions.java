package org.domainspecific.dsl.bdd;

public class Conditions {
    private final Object actualValue;

    public Conditions(Object actualValue) {
        this.actualValue = actualValue;
    }

    public void shouldBe(String expected) {
        if (!actualValue.equals(expected)) {
            throw new AssertionError(String.format("Expected %s but found %s", expected, actualValue));
        }
    }

    public void shouldBe(int expected) {
        if (!actualValue.equals(expected)) {
            throw new AssertionError(String.format("Expected %s but found %s", expected, actualValue));
        }
    }
}
