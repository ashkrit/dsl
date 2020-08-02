package org.domainspecific.dsl.ruleengine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RuleSchema<T> {
    private final String name;
    private final Map<String, Function<T, Object>> schema = new HashMap<>();

    public RuleSchema(String name) {
        this.name = name;
    }

    public static <T> RuleSchema<T> schema(String name, SchemaField<T> schema) {
        RuleSchema<T> s = new RuleSchema<>(name);
        schema.apply(s);
        return s;
    }

    public void attribute(String name, Function<T, Object> f) {
        schema.put(name, f);
    }

    private Function<T, Object> field(String field) {
        return schema.get(field);
    }

    public Object fieldValue(String field, T row) {
        if (row == null) return null;
        return field(field).apply(row);
    }

    interface SchemaField<T> {
        void apply(RuleSchema<T> r);
    }

}
