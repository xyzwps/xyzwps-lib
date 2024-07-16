package com.xyzwps.lib.ap;

import com.google.auto.service.AutoService;
import com.xyzwps.lib.ap.api.ApiAPOpenapiGen;
import com.xyzwps.lib.ap.api.ApiAPRouterGen;
import com.xyzwps.lib.ap.dsl.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.xyzwps.lib.ap.API")
public class ApiAP extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            for (TypeElement annotation : annotations) {
                for (var element : roundEnv.getElementsAnnotatedWith(annotation)) {
                    if (element instanceof TypeElement typeElement) {
                        System.out.println("🚩ApiAP Found class: " + typeElement);

                        var routerGen = new ApiAPRouterGen(typeElement);
                        routerGen.generate(this::writeJavaFile);

                        var openapiGen = new ApiAPOpenapiGen(typeElement);
                        openapiGen.generate();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("🚩ApiAP Error: " + e.getMessage());
            e.printStackTrace(System.out);
            throw e;
        }
        return true;
    }

    private void writeJavaFile($Type generatedClassName, String sourceCode) {
        try {
            var sourceFile = processingEnv.getFiler().createSourceFile(generatedClassName.getFullName());
            try (var out = new PrintWriter(sourceFile.openWriter())) {
                out.write(sourceCode);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
