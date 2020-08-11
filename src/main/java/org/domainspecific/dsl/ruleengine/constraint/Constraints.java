package org.domainspecific.dsl.ruleengine.constraint;

public interface Constraints<T> {
    void apply(Constraint<T> constraint);
}
