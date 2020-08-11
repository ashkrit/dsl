package org.domainspecific.dsl.bdd;

import java.util.function.Consumer;

public class Specification {
    private final String name;

    public Specification(String name) {
        this.name = name;
    }

    public static void specification(String name, Consumer<Specification> suite) {
        Specification spec = new Specification(name);
        suite.accept(spec);
    }

    public void should(String name, Consumer<Matchers> scenario) {
        Matchers m = new Matchers(name, new SpecificationResult());
        try {
            scenario.accept(m);
            SpecificationResult.pass.add(new TestResult(this.name, name, null, null));
        } catch (AssertionError e) {
            SpecificationResult.fail.add(new TestResult(this.name, name, e, null));
        } catch (Exception e) {
            SpecificationResult.errors.add(new TestResult(this.name, name, null, e));
        }
    }

}
