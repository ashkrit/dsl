package org.domainspecific.dsl.ruleengine;

import org.domainspecific.dsl.ruleengine.constraint.Conditions;
import org.domainspecific.dsl.ruleengine.constraint.Constraint;
import org.domainspecific.dsl.ruleengine.constraint.Constraints;

import java.util.ArrayList;
import java.util.List;

public class DecisionSystem<T> {

    private final String name;
    private final RuleSchema<T> schema;
    private final List<Constraint<T>> constraints = new ArrayList<>();

    public DecisionSystem(String name, RuleSchema<T> schema) {
        this.name = name;
        this.schema = schema;
    }

    public static <T> DecisionSystem<T> create(String name, RuleSchema<T> schema, Conditions<T> conditions) {
        DecisionSystem<T> ds = new DecisionSystem<>(name, schema);
        conditions.apply(ds);
        return ds;
    }


    public void rule(String ruleName, Constraints<T> c) {
        Constraint<T> r = new Constraint<>(ruleName, schema);
        c.apply(r);
        constraints.add(r);
    }

    public void process(T record) {
        constraints
                .stream()
                .filter(constraint -> constraint.match(record))
                .forEach(constraint -> {
                    constraint.apply(record);
                });
    }

}
