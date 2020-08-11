package org.domainspecific.dsl.main.bdd;

import org.domainspecific.dsl.bdd.SpecificationResult;

public class SpecRunner {

    public static void main(String[] args) {

        new ExampleSpec();

        System.out.println("Passed Run \n " + SpecificationResult.passedTestCase());
        System.out.println("Failed Test \n " + SpecificationResult.failedTestCase());
        System.out.println("Error Test \n " + SpecificationResult.errorTestCase());


    }
}
