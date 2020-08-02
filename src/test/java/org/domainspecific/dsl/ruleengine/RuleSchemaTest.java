package org.domainspecific.dsl.ruleengine;

import org.junit.jupiter.api.Test;

import static org.domainspecific.dsl.ruleengine.RuleSchema.schema;
import static org.junit.Assert.assertEquals;

public class RuleSchemaTest {

    @Test
    public void register_schema_fields() {
        RuleSchema<Trade> tradeSchema = schema("Trade", schema -> {
            schema.attribute("symbol", Trade::getSymbol);
            schema.attribute("tradeType", Trade::getTradeType);
            schema.attribute("unit", Trade::getUnit);
            schema.attribute("price", Trade::getPrice);
        });

        Trade t = new Trade("GOOG", "BUY", 100, 1490.5);
        assertEquals("GOOG", tradeSchema.fieldValue("symbol", t));
        assertEquals("BUY", tradeSchema.fieldValue("tradeType", t));
        assertEquals(100, tradeSchema.fieldValue("unit", t));
        assertEquals(1490.5, tradeSchema.fieldValue("price", t));
    }


    static class Trade {
        private String symbol;
        private String tradeType;
        private int unit;
        private double price;

        Trade(String symbol, String tradeType, int unit, double price) {
            this.symbol = symbol;
            this.tradeType = tradeType;
            this.unit = unit;
            this.price = price;
        }

        public double getPrice() {
            return price;
        }

        public int getUnit() {
            return unit;
        }

        public String getSymbol() {
            return symbol;
        }

        public String getTradeType() {
            return tradeType;
        }
    }
}
