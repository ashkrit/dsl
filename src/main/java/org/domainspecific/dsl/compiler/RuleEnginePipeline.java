package org.domainspecific.dsl.compiler;

import org.domainspecific.dsl.main.ruleengine.FXTransaction;
import org.domainspecific.dsl.ruleengine.RuleEngine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

public class RuleEnginePipeline {

    public static Function<CompilationContext, RuleEngine<FXTransaction>> pipeline() {
        return loadTemplate
                .andThen(loadDsl)
                .andThen(codeGeneration)
                .andThen(compileCode)
                .andThen(loadRules);
    }

    private static Function<CompilationContext, CompilationContext> loadTemplate = context -> {
        String result;
        try {
            URL in = RuleEnginePipeline.class.getResource(String.format("/ruleengine/%s.java", context.className));
            result = new String(Files.readAllBytes(Paths.get(in.toURI())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        context.template = result;
        return context;
    };

    private static Function<CompilationContext, CompilationContext> loadDsl = context -> {
        try {
            context.newCode = new String(Files.readAllBytes(Paths.get(context.dsl)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return context;
    };

    private static Function<CompilationContext, CompilationContext> codeGeneration = context -> {
        String template = context.template;
        template = template.replace("//CODE", "ds=" + context.newCode);
        template = template.replace("$REPLACE", context.generatedName);
        context.generatedCode = template;
        System.out.println(context.generatedCode);
        return context;
    };

    private static Function<CompilationContext, CompilationContext> compileCode = context -> {
        List<InMemoryJavaCompiler.CompilerOutput> result = InMemoryJavaCompiler.compile(context.generatedClassName, context.generatedCode);
        result.stream().forEach(d -> System.out.format("Line: %d, %s in %s", d.lineNumber, d.message, d.sourceFile));
        return context;
    };

    private static Function<CompilationContext, RuleEngine<FXTransaction>> loadRules = context -> {
        try {
            Class cls = Class.forName(context.generatedClassName, true, InMemoryJavaCompiler.classLoader);
            System.out.println(cls);
            Method m = cls.getDeclaredMethod("create", new Class[]{});
            Object ruleObject = cls.newInstance();
            return (RuleEngine<FXTransaction>) m.invoke(ruleObject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };


}
