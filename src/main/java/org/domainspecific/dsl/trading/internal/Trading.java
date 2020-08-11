package org.domainspecific.dsl.trading.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Trading {
    public static final TradingSystem ts = new TradingSystem();

    public static Order newOrder(Instrument security, BiConsumer<Order, TradingSystem> consumer) {
        Order order = new Order(security);
        consumer.accept(order, ts);
        return order;
    }

    public static Equity equity(String id) {
        return new Equity(id);
    }

    public static FixedIncome fixedIncome(String id) {
        return new FixedIncome(id);
    }

    public static class TradingSystem {
        public List<Order> orders = new ArrayList<>();

        public void execute(Order order) {
            orders.add(order);
        }
    }

    public static class Order {

        private final Instrument security;
        private String tradeType;
        private int unit;
        private double price;
        private boolean full = true;

        public Order(Instrument security) {
            this.security = security;
        }

        public Order buy(int unit) {
            this.tradeType = "BUY";
            this.unit = unit;
            return this;
        }

        public Order at(double price) {
            this.price = price;
            return this;
        }

        public Order sell(int unit) {
            this.tradeType = "SELL";
            this.unit = unit;
            return this;
        }

        public Order partial() {
            this.full = false;
            return this;
        }

        @Override
        public String toString() {
            return String.format("(%s;%s;%s;%s)", security, tradeType, unit, price);
        }
    }

    static abstract class Instrument {
    }

    static class Equity extends Instrument {
        public final String security;

        Equity(String security) {
            this.security = security;
        }

        @Override
        public String toString() {
            return security;
        }
    }

    static class FixedIncome extends Instrument {
        public final String security;
        /*
        public final LocalDate issueDate;
        public final LocalDate maturityDate;
        public final double rate;
         */

        FixedIncome(String security) {
            this.security = security;
        }
    }

}
