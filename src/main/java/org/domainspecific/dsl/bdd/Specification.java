package org.domainspecific.dsl.bdd;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class Specification {
    private final String name;

    public Specification(String name) {
        this.name = name;
    }

    public static void specification(String name, Suite suite) {
        Specification spec = new Specification(name);
        suite.apply(spec);
    }

    public void should(String name, Scenario scenario) {
        Matchers m = new Matchers(name, new SpecificationResult(), scenario);
        String scenarioName = this.name + "." + name;

        try {
            scenario.apply(m);
            SpecificationResult.pass.add(scenarioName);
        } catch (AssertionError e) {
            SpecificationResult.fail.put(scenarioName, e);
        } catch (Exception e) {
            SpecificationResult.errors.put(scenarioName, e);
        }
    }

    public interface Suite {
        void apply(Specification s);
    }

    public interface Scenario {
        void apply(Matchers s);
    }

    public static class Matchers {

        private final String name;
        private final SpecificationResult specification;
        private final Scenario scenario;

        public Matchers(String name, SpecificationResult specification, Scenario scenario) {
            this.name = name;
            this.specification = specification;
            this.scenario = scenario;
        }


        public Matcher value(Object value) {
            return new Matcher(value);
        }

        public void shouldThrow(Class<? extends Exception> exception, Supplier<Object> s) {
            try {
                s.get();
                throw new AssertionError("No Exception thrown");
            } catch (Exception e) {
                if (!(e.getClass() == exception)) {
                    throw new AssertionError(String.format("Expected %s but found %s", exception, e.getClass()));
                }
            }
        }
    }

    public static class Matcher {
        private final Object value;

        public Matcher(Object value) {
            this.value = value;
        }

        public void shouldBe(String expected) {
            if (!value.equals(expected)) {
                throw new AssertionError(String.format("Expected %s but found %s", expected, value));
            }
        }

        public void shouldBe(int expected) {
            if (!value.equals(expected)) {
                throw new AssertionError(String.format("Expected %s but found %s", expected, value));
            }
        }
    }

}
