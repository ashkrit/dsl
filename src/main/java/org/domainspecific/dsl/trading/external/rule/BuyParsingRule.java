package org.domainspecific.dsl.trading.external.rule;

import org.domainspecific.dsl.trading.internal.Trading;
import org.domainspecific.dsl.trading.external.ParserArgs;
import org.domainspecific.dsl.trading.external.ParsingRule;

import static org.domainspecific.dsl.trading.internal.Trading.equity;

public class BuyParsingRule implements ParsingRule<Trading.Order> {

    public boolean match(String keyword) {
        return "buy".equals(keyword);
    }

    @Override
    public void apply(ParserArgs<Trading.Order> p) {
        if (p.buffer.size() == 3) {
            p.value = new Trading.Order(equity(p.buffer.get(1)));
            p.value.buy(Integer.parseInt(p.buffer.get(0)));
            p.reset();
        }
    }
}
