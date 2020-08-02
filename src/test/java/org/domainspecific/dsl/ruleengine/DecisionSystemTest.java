package org.domainspecific.dsl.ruleengine;

import org.junit.jupiter.api.Test;

import static org.domainspecific.dsl.ruleengine.RuleSchema.schema;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecisionSystemTest {

    @Test
    public void simple_rule_engine() {

        RuleSchema<FXTransaction> tradeSchema = schema("Trade", schema -> {
            schema.attribute("source", FXTransaction::getSource);
            schema.attribute("target", FXTransaction::getTarget);
            schema.attribute("amount", FXTransaction::getAmount);
            schema.attribute("date", FXTransaction::getTransactionDate);
            schema.attribute("bankCode", FXTransaction::getBankCode);
            schema.attribute("accountNo", FXTransaction::getAccountNum);
        });

        DecisionSystem<FXTransaction> ds = DecisionSystem.create("FX Transaction", tradeSchema, s -> {
            s.rule("High Transfer discount", c -> {
                c
                        .gt("amount", 1000d)
                        .action((rule, row) -> row.setDiscount(2.0d));

            });
        });

        FXTransaction tran = new FXTransaction(
                "SGD", "INR", 5000, 20200805, "ICICIBANK", "1111-222-6543");

        ds.process(tran);

        assertEquals(2.0, tran.getDiscount());

    }
}
