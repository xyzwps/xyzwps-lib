package com.xyzwps.lib.ap;

import com.google.auto.service.AutoService;
import com.xyzwps.lib.ap.api.ApiAPOpenapiGen;
import com.xyzwps.lib.ap.api.ApiAPRouterGen;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.*;
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
                        routerGen.generate(processingEnv);

                        var openapiGen = new ApiAPOpenapiGen(typeElement);
                        openapiGen.generate(processingEnv);
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
}
