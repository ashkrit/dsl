package org.domainspecific.dsl.compiler;

public class CompilationContext {
    public final String className;
    public final String generatedName;
    public final String generatedClassName;
    public final String dsl;

    public String template;
    public String dslCode;
    public String generatedCode;

    public CompilationContext(String className, String dsl) {
        this.className = className;
        this.dsl = dsl;
        this.generatedName = String.format("$GENERATED$%s", System.currentTimeMillis());
        this.generatedClassName = String.format("FXTradeTemplate%s", generatedName);
    }
}
