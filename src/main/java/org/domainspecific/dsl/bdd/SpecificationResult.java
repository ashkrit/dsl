package org.domainspecific.dsl.bdd;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SpecificationResult {

    public static Set<String> pass = new LinkedHashSet<>();
    public static Map<String, AssertionError> fail = new LinkedHashMap<>();
    public static Map<String, Exception> errors = new LinkedHashMap<>();

}
