package org.domainspecific.dsl.ruleengine.constraint;

import org.domainspecific.dsl.ruleengine.constraint.Constraint;

public interface Constraints<T> {
    void apply(Constraint<T> constraint);
}
