package org.domainspecific.dsl.trading;

import org.domainspecific.dsl.trading.internal.Trading;

import static org.domainspecific.dsl.trading.internal.Trading.*;

public class InternalTradingApp {

    public static void main(String[] args) {

        newOrder(equity("IBM"), (order, ts) -> {
            order
                    .buy(100)
                    .at(126.07d);

            ts.execute(order);
        });

        newOrder(equity("GOOG"), (order, ts) -> {
            order
                    .sell(100)
                    .at(1506.85)
                    .partial();

            ts.execute(order);
        });

        System.out.println(Trading.ts.orders);

    }
}
