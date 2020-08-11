package org.domainspecific.dsl.trading.external.rule;

import org.domainspecific.dsl.trading.internal.Trading;
import org.domainspecific.dsl.trading.external.ParserArgs;
import org.domainspecific.dsl.trading.external.ParsingRule;

public class AtParsingRule implements ParsingRule<Trading.Order> {

    public boolean match(String keyword) {
        return "at".equals(keyword);
    }

    @Override
    public void apply(ParserArgs<Trading.Order> p) {
        if (p.buffer.size() == 1) {
            p.value.at(Double.parseDouble(p.buffer.get(0)));
            p.completed = true;
        }
    }
}
