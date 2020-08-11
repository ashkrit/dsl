package org.domainspecific.dsl.compiler;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class InMemoryJavaCompiler {

    public static ClassLoader classLoader = executionPathClassloader();

    public static ClassLoader executionPathClassloader() {
        try {
            URL url = new File("").toURI().toURL();
            return URLClassLoader.newInstance(new URL[]{url});
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<CompilerOutput> compile(String className, String code) {
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> ds = new DiagnosticCollector<>();

            try (StandardJavaFileManager mgr = compiler.getStandardFileManager(ds, null, null)) {

                JavaFileObject file = new InMemoryJavaCode(className, code);
                Iterable<? extends JavaFileObject> sources = Arrays.asList(file);

                CompilationTask task = compiler.getTask(null, mgr, ds, null, null, sources);
                task.call();
            }

            return ds.getDiagnostics().stream()
                    .map(InMemoryJavaCompiler::toCompilerOutput)
                    .collect(toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static CompilerOutput toCompilerOutput(Diagnostic<? extends JavaFileObject> x) {
        return new CompilerOutput(x.getLineNumber(), x.getMessage(null), x.getSource().getName());
    }

    static class InMemoryJavaCode extends SimpleJavaFileObject {
        final String code;

        InMemoryJavaCode(String name, String code) {
            super(URI.create("memory:///" + name + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    public static class CompilerOutput {
        public final long lineNumber;
        public final String message;
        public final String sourceFile;

        CompilerOutput(long lineNumber, String message, String sourceFile) {
            this.lineNumber = lineNumber;
            this.message = message;
            this.sourceFile = sourceFile;
        }
    }

}
