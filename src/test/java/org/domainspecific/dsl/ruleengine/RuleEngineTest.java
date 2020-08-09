package org.domainspecific.dsl.ruleengine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.domainspecific.dsl.ruleengine.RuleEngine.decisionSystem;
import static org.domainspecific.dsl.ruleengine.RuleSchema.schema;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Rule engine spec")
public class RuleEngineTest {

    RuleSchema<FXTransaction> tradeSchema = schema("Trade", schema -> {
        schema.attribute("source", FXTransaction::getSource);
        schema.attribute("target", FXTransaction::getTarget);
        schema.attribute("amount", FXTransaction::getAmount);
        schema.attribute("date", FXTransaction::getTransactionDate);
        schema.attribute("bankCode", FXTransaction::getBankCode);
        schema.attribute("accountNo", FXTransaction::getAccountNum);
    });

    @Test
    @DisplayName("Rule engine with single rule")
    public void simple_rule_engine() {

        RuleEngine<FXTransaction> ds = decisionSystem("FX Transaction", tradeSchema, s -> {

            s.rule("High Transfer discount", condition -> {
                condition
                        .gt("amount", 1000d)
                        .action((rule, row) -> {
                            row.setDiscount(2.0d);
                            System.out.println(String.format("qualified for rule '%s' ", rule));
                        });

            });


        });

        FXTransaction tran = new FXTransaction(
                "SGD", "INR", 5000, 20200805, "ICICIBANK", "1111-222-6543");

        ds.process(tran);

        assertEquals(2.0, tran.getDiscount());

    }


    @Test
    @DisplayName("Rule engine with multiple rules")
    public void rule_engine_with_multiple_rules() {

        RuleEngine<FXTransaction> ds = decisionSystem("FX Transaction", tradeSchema, s -> {

            s.rule("High Transfer discount", condition -> {
                condition
                        .gt("amount", 1000d)
                        .eq("source", "SGD")
                        .eq("target", "INR")
                        .action((rule, row) -> row.setDiscount(2.0d));
            });

            s.rule("less than minimum", condition -> {
                condition
                        .lt("amount", 1000d)
                        .action((rule, row) -> row.setDiscount(-1.0d));
            });


            s.rule("limited time period", conditions -> {
                        conditions
                                .between("date", 20200805, 20200831)
                                .action((rule, row) -> row.setDiscount(5.0d));
                    }
            );

        });

        FXTransaction tran1 = new FXTransaction(
                "SGD", "INR", 5000, 20200801, "SafeBANK", "1111-222-6543");
        FXTransaction tran2 = new FXTransaction(
                "SGD", "INR", 800, 20200801, "SafeBANK", "1111-222-6543");
        FXTransaction tran3 = new FXTransaction(
                "SGD", "INR", 5000, 20200806, "SafeBANK", "1111-222-6543");


        ds.process(tran1);
        ds.process(tran2);
        ds.process(tran3);

        assertEquals(2.0, tran1.getDiscount());
        assertEquals(-1.0, tran2.getDiscount());
        assertEquals(5.0, tran3.getDiscount());

    }

}
