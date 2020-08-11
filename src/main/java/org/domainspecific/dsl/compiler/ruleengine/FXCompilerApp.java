package org.domainspecific.dsl.compiler.ruleengine;

import org.domainspecific.dsl.compiler.CompilationContext;
import org.domainspecific.dsl.compiler.InMemoryJavaCompiler;
import org.domainspecific.dsl.main.ruleengine.FXTransaction;
import org.domainspecific.dsl.ruleengine.RuleEngine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

public class FXCompilerApp {


    public static void main(String[] args) throws Exception {

        String dsl = args[0];
        System.out.println(dsl);


        CompilationContext context = new CompilationContext("FXTradeTemplate", dsl);

        Function<CompilationContext, CompilationContext> loadTemplate = c -> {
            c.template = loadTemplate(c);
            return c;
        };

        Function<CompilationContext, CompilationContext> loadDsl = c -> {
            c.newCode = loadDSLCode(c.dsl);
            return c;
        };

        Function<CompilationContext, CompilationContext> codeGeneration = c -> {
            c.generatedCode = generateCode(c.template, c.newCode, c.generatedName);
            System.out.println(context.generatedCode);
            return c;
        };

        Function<CompilationContext, CompilationContext> compileCode = c -> {
            List<InMemoryJavaCompiler.CompilerOutput> result = InMemoryJavaCompiler.compile(c.generatedClassName, c.generatedCode);
            result.stream().forEach(d -> System.out.format("Line: %d, %s in %s", d.lineNumber, d.message, d.sourceFile));
            return c;
        };


        Function<CompilationContext, RuleEngine<FXTransaction>> load = c -> loadRules(c.generatedClassName);


        RuleEngine<FXTransaction> rules = loadTemplate
                .andThen(loadDsl)
                .andThen(codeGeneration)
                .andThen(compileCode)
                .andThen(load)
                .apply(context);

        System.out.println(rules);

    }

    private static RuleEngine<FXTransaction> loadRules(String className) {
        try {
            Class cls = Class.forName(className, true, InMemoryJavaCompiler.classLoader);
            System.out.println(cls);
            Method m = cls.getDeclaredMethod("create", new Class[]{});
            Object ruleObject = cls.newInstance();
            return (RuleEngine<FXTransaction>) m.invoke(ruleObject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateCode(String template, String newCode, String fileName) {
        template = template.replace("//CODE", "ds=" + newCode);
        template = template.replace("$REPLACE", fileName);
        return template;
    }

    private static String loadDSLCode(String dsl) {
        try {
            return new String(Files.readAllBytes(Paths.get(dsl)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String loadTemplate(CompilationContext context) {
        try {
            URL in = FXCompilerApp.class.getResource(String.format("/ruleengine/%s.java", context.className));
            return new String(Files.readAllBytes(Paths.get(in.toURI())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
