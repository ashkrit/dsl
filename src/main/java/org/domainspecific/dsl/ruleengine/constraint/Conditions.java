package org.domainspecific.dsl.ruleengine.constraint;

import org.domainspecific.dsl.ruleengine.RuleEngine;

public interface Conditions<T> {
    void apply(RuleEngine<T> ds);
}
