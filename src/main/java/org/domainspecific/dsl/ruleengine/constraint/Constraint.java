package org.domainspecific.dsl.ruleengine.constraint;

import org.domainspecific.dsl.ruleengine.RuleSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class Constraint<T> {

    private final String ruleName;
    private final RuleSchema<T> schema;
    private final List<Predicate<T>> predicates = new ArrayList<>();
    private final List<BiConsumer<String, T>> consumers = new ArrayList<>();

    public Constraint(String ruleName, RuleSchema<T> schema) {
        this.ruleName = ruleName;
        this.schema = schema;
    }

    public Constraint<T> gt(String field, double value) {
        predicates.add(r -> (double) schema.fieldValue(field, r) > value);
        return this;
    }

    public Constraint<T> eq(String field, String value) {
        predicates.add(r -> schema.fieldValue(field, r).equals(value));
        return this;
    }

    public Constraint<T> action(BiConsumer<String, T> consumer) {
        consumers.add(consumer);
        return this;
    }

    public Constraint<T> lt(String field, int value) {
        predicates.add(r -> (Integer) schema.fieldValue(field, r) < value);
        return this;
    }

    public Constraint<T> between(String field, long from, long to) {
        predicates.add(r -> {
            Integer value = (Integer) schema.fieldValue(field, r);
            return value >= from && value <= to;
        });
        return this;
    }

    public boolean match(T record) {
        int expectedCount = predicates.size();
        long actualCount = predicates
                .stream()
                .filter(p -> p.test(record)).count();
        return expectedCount == actualCount;
    }

    public void apply(T record) {
        consumers.stream().forEach(c -> c.accept(ruleName, record));
    }
}
