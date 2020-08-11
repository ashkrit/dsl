
import org.domainspecific.dsl.main.ruleengine.FXTransaction;
import org.domainspecific.dsl.ruleengine.RuleEngine;
import org.domainspecific.dsl.ruleengine.RuleSchema;
import static org.domainspecific.dsl.ruleengine.RuleEngine.*;

import static org.domainspecific.dsl.ruleengine.RuleSchema.schema;

public class FXTradeTemplate$REPLACE {

    static RuleSchema<FXTransaction> tradeSchema = schema("Trade", schema -> {
        schema.attribute("source", FXTransaction::getSource);
        schema.attribute("target", FXTransaction::getTarget);
        schema.attribute("amount", FXTransaction::getAmount);
        schema.attribute("date", FXTransaction::getTransactionDate);
        schema.attribute("bankCode", FXTransaction::getBankCode);
        schema.attribute("accountNo", FXTransaction::getAccountNum);
    });

    public RuleEngine<FXTransaction> create() {
        RuleEngine<FXTransaction> ds = null;
        //CODE
        return ds;
    }


}
