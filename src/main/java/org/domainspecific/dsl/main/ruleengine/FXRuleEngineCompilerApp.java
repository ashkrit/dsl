package org.domainspecific.dsl.main.ruleengine;

import org.domainspecific.dsl.compiler.CompilationContext;
import org.domainspecific.dsl.compiler.RuleEnginePipeline;
import org.domainspecific.dsl.ruleengine.RuleEngine;

import java.util.function.Function;

public class FXRuleEngineCompilerApp {

    public static void main(String[] args) {

        String dsl = args[0];
        System.out.println(dsl);

        CompilationContext context = new CompilationContext("FXTradeTemplate", dsl);

        Function<CompilationContext, RuleEngine<FXTransaction>> pipeline = RuleEnginePipeline.pipeline();

        RuleEngine<FXTransaction> rules = pipeline.apply(context);

        System.out.println(rules.name);
        System.out.println(rules.constraints);


    }

}
