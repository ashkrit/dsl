package org.domainspecific.dsl.main.ruleengine;

import org.domainspecific.dsl.ruleengine.RuleEngine;
import org.domainspecific.dsl.ruleengine.RuleSchema;

import static org.domainspecific.dsl.ruleengine.RuleEngine.decisionSystem;
import static org.domainspecific.dsl.ruleengine.RuleSchema.schema;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RuleEngineApp {

    public static void main(String[] args) {
        RuleSchema<FXTransaction> tradeSchema = schema("Trade", schema -> {
            schema.attribute("source", FXTransaction::getSource);
            schema.attribute("target", FXTransaction::getTarget);
            schema.attribute("amount", FXTransaction::getAmount);
            schema.attribute("date", FXTransaction::getTransactionDate);
            schema.attribute("bankCode", FXTransaction::getBankCode);
            schema.attribute("accountNo", FXTransaction::getAccountNum);
        });


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

        System.out.println("Tran 1 -> " + tran1.getDiscount());
        System.out.println("Tran 2 -> " + tran2.getDiscount());
        System.out.println("Tran 3 ->" + tran3.getDiscount());

    }
}
