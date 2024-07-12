package com.xyzwps.lib.ap;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.xyzwps.lib.ap.Dao")
public final class DaoAP extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : annotatedElements) {
                if (element instanceof TypeElement typeElement) {
                    var daoClassName = typeElement.getQualifiedName().toString();
                    System.out.println("🚩DaoAP Found class: " + daoClassName);

                    var i = daoClassName.lastIndexOf(".");
                    var packageName = daoClassName.substring(0, i);
                    var simpleName = daoClassName.substring(i + 1);

                    var dbBeanId = typeElement.getAnnotation(Dao.class).value();
                    var generatedClassName = daoClassName + "Provider";
                    try {
                        var sourceFile = processingEnv.getFiler().createSourceFile(generatedClassName);
                        generateProvider(sourceFile, packageName, simpleName, dbBeanId);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            }
        }

        return true;
    }

    private static void generateProvider(JavaFileObject sourceFile, String packageName, String simpleName, String dbBeanId) throws IOException {
        try (var out = new PrintWriter(sourceFile.openWriter())) {
            var hasDbName = dbBeanId != null && dbBeanId.isBlank();

            out.println("package " + packageName + ";");
            out.println();
            out.println("import com.xyzwps.lib.jdbc.DaoFactory;");
            out.println("import com.xyzwps.lib.jdbc.Database;");
            if (hasDbName) {
                out.println("import jakarta.inject.Named;");
            }
            out.println("import jakarta.inject.Provider;");
            out.println("import jakarta.inject.Singleton;");
            out.println();
            out.println("@Singleton");
            out.println("public class " + simpleName + "Provider implements Provider<" + simpleName + "> {");
            out.println();
            out.println("    private final Database db;");
            out.println();
            var dbName = hasDbName ? "" : "@Named(\"" + dbBeanId + "\") ";
            out.println("    public " + simpleName + "Provider(" + dbName + "Database db) {");
            out.println("        this.db = db;");
            out.println("    }");
            out.println();
            out.println("    @Override");
            out.println("    public " + simpleName + " get() {");
            out.println("        return DaoFactory.createDao(" + simpleName + ".class, db::autoCommitTransactionContext);");
            out.println("    }");
            out.println("}");
        }

    }
}
