package org.domainspecific.dsl.trading.external;

public interface ParsingRule<T> {
    boolean match(String keyword);

    void apply(ParserArgs<T> args);
}
