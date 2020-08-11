package org.domainspecific.dsl.bdd;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class SpecificationResult {

    public static final Set<TestResult> pass = new LinkedHashSet<>();
    public static final Set<TestResult> fail = new LinkedHashSet<>();
    public static final Set<TestResult> errors = new LinkedHashSet<>();

    public static String passedTestCase() {
        return testSummary(pass.stream());
    }

    public static String failedTestCase() {
        return testSummary(fail.stream());
    }

    public static String errorTestCase() {
        return testSummary(errors.stream());
    }

    private static String testSummary(Stream<TestResult> stream) {
        Map<String, List<TestResult>> groupBySpec = stream.collect(groupingBy(TestResult::getSpec));

        return groupBySpec.entrySet().stream()
                .map(SpecificationResult::toScenario)
                .collect(Collectors.joining("\n"));
    }


    private static String toScenario(Map.Entry<String, List<TestResult>> testResult) {

        StringBuilder sb = new StringBuilder();
        sb.append(testResult.getKey())
                .append("\t")
                .append("\n\t");

        String scenario = testResult.getValue()
                .stream()
                .map(r -> toMessage(r))
                .collect(Collectors.joining("\n\t"));

        sb.append(scenario);

        return sb.toString();
    }

    private static String toMessage(TestResult r) {
        return r.scenario + errorMessage(r.getFail()) + errorMessage(r.getError());
    }

    private static String errorMessage(Optional<Throwable> fail) {
        return fail.map(v -> " -> " + v.toString()).orElse("");
    }
}
