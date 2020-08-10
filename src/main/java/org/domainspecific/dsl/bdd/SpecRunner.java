package org.domainspecific.dsl.bdd;

import java.util.stream.Collectors;

public class SpecRunner {

    public static void main(String[] args) {

        new ExampleSpec();

        System.out.println("Passed Run \n " + SpecificationResult.pass.stream().collect(Collectors.joining("\n ")));
        System.out.println("Failed Test \n " + SpecificationResult.fail.entrySet().stream().map(k -> k.getKey() + "=>" + k.getValue()).collect(Collectors.joining("\n ")));
        System.out.println("Error Test \n " + SpecificationResult.errors.entrySet().stream().map(k -> k.getKey() + "=>" + k.getValue()).collect(Collectors.joining("\n ")));


    }
}
