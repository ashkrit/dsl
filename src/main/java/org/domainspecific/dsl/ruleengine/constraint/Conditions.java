package org.domainspecific.dsl.ruleengine.constraint;

import org.domainspecific.dsl.ruleengine.DecisionSystem;

public interface Conditions<T> {
    void apply(DecisionSystem<T> ds);
}
