package org.domainspecific.dsl.bdd.examples;

import org.domainspecific.dsl.bdd.SpecificationResult;
import org.domainspecific.dsl.bdd.examples.ExampleSpec;

import java.util.stream.Collectors;

public class SpecRunner {

    public static void main(String[] args) {

        new ExampleSpec();

        System.out.println("Passed Run \n " + SpecificationResult.passedTestCase());
        System.out.println("Failed Test \n " + SpecificationResult.failedTestCase());
        System.out.println("Error Test \n " + SpecificationResult.errorTestCase());


    }
}
