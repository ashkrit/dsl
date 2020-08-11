package org.domainspecific.dsl.bdd;

import java.util.Optional;

public class TestResult {
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
