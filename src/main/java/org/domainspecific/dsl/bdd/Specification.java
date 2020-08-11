package org.domainspecific.dsl.bdd;

import java.util.Optional;
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
        try {
            scenario.apply(m);
            SpecificationResult.pass.add(new TestResult(this.name, name, null, null));
        } catch (AssertionError e) {
            SpecificationResult.fail.add(new TestResult(this.name, name, e, null));
        } catch (Exception e) {
            SpecificationResult.errors.add(new TestResult(this.name, name, null, e));
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

    public static class TestResult {
        public final String spec;
        public final String scenario;
        public final Throwable fail;
        public final Throwable error;

        public TestResult(String spec, String scenario, AssertionError fail, Exception error) {
            this.spec = spec;
            this.scenario = scenario;
            this.error = error;
            this.fail = fail;
        }

        public String getScenario() {
            return scenario;
        }

        public String getSpec() {
            return spec;
        }

        public Optional<Throwable> getFail() {
            return Optional.ofNullable(fail);
        }

        public Optional<Throwable> getError() {
            return Optional.ofNullable(error);
        }
    }

}
