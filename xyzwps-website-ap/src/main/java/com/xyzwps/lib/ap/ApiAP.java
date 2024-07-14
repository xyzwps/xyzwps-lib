package com.xyzwps.lib.ap;

import com.google.auto.service.AutoService;
import com.xyzwps.lib.ap.dsl.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.*;
import javax.lang.model.element.Element;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.xyzwps.lib.ap.dsl.AccessLevel.*;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.xyzwps.lib.ap.API")
public class ApiAP extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : annotatedElements) {
                if (element instanceof TypeElement typeElement) {
                    var typeClassName = typeElement.getQualifiedName().toString();
                    var apiPrefix = typeElement.getAnnotation(API.class).value();
                    System.out.println("🚩ApiAP Found class: " + typeClassName + ". " + apiPrefix);
                    var methods = typeElement.getEnclosedElements()
                            .stream()
                            .filter(it -> {
                                var modifiers = it.getModifiers();
                                return modifiers.contains(Modifier.PUBLIC)
                                        && !modifiers.contains(Modifier.STATIC)
                                        && !modifiers.contains(Modifier.ABSTRACT);
                            })
                            .flatMap(it -> it instanceof ExecutableElement executableElement
                                    ? Stream.of(executableElement) : Stream.of())
                            .filter(it -> !it.getSimpleName().toString().equals("<init>"))
                            .collect(Collectors.toList());
                    if (!methods.isEmpty()) {
                        var source = generateRouterClass(typeElement, methods);
                        writeJavaFile(source.getKey(), source.getValue());
                    }
                }
            }
        }
        return true;
    }

    private void writeJavaFile(FullTypeNameElement generatedClassName, String sourceCode) {
        try {
            var sourceFile = processingEnv.getFiler().createSourceFile(generatedClassName.getFullName());
            try (var out = new PrintWriter(sourceFile.openWriter())) {
                out.write(sourceCode);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Map.Entry<FullTypeNameElement, String> generateRouterClass(TypeElement typeElement, List<ExecutableElement> methods) {
        var daoClassName = typeElement.getQualifiedName().toString();
        var i = daoClassName.lastIndexOf(".");
        var packageName = daoClassName.substring(0, i);
        var simpleName = daoClassName.substring(i + 1);

        var apiPrefix = typeElement.getAnnotation(API.class).value();

        var generatedClassName = simpleName + "RouterAP";

        var generatedClassType = new FullTypeNameElement(packageName, generatedClassName);
        var apiClassType = new FullTypeNameElement(packageName, simpleName);
        var annoSingleton = new FullTypeNameElement("jakarta.inject", "Singleton");
        var routerType = new FullTypeNameElement("com.xyzwps.lib.express.filter", "Router");
        var httpMethodType = new FullTypeNameElement("com.xyzwps.lib.express", "HttpMethod");
        var jsonType = new FullTypeNameElement("com.xyzwps.website.common", "JSON");
        var routerMakerType = new FullTypeNameElement("com.xyzwps.website.filter", "RouterMaker");

        var buildApisMethod = new MethodElement(null, "make")
                .addArgument(new ArgumentElement(routerType, "router"))
                .addLine("router");
        handleMethods(methods, apiPrefix, buildApisMethod);
        buildApisMethod.addLine(";");

        var generatedClass = new ClassElement(generatedClassType)
                .shouldBePublic()
                .addAnnotation(new AnnotationElement(annoSingleton))
                .addImplementedInterface(routerMakerType)
                .addField(new FieldElement(apiClassType, "apis").accessLevel(PRIVATE).shouldBeFinal())
                .addImport(httpMethodType)
                .addImport(jsonType)
                .addMethod(buildApisMethod);
        var toJavaClass = new ToJavaClassVisitor();
        generatedClass.visit(toJavaClass);
        return Map.entry(generatedClassType, toJavaClass.toJavaClass());
    }

    private static void handleMethods(List<ExecutableElement> executableElements, String apiPrefix, MethodElement methodElement) {
        for (var method : executableElements) {
            handleMethod(method, apiPrefix, methodElement);
        }
    }

    private static void handleMethod(ExecutableElement method, String apiPrefix, MethodElement e) {
        var apiInfo = getApiInfo(method);
        e.addLine("    .handle(HttpMethod.%s, \"%s\", (req, res, next) -> {", apiInfo.method, apiPrefix + apiInfo.path);

        var params = method.getParameters();
        var argNames = new ArrayList<String>();
        for (var param : params) {
            {
                var searchParam = param.getAnnotation(SearchParam.class);
                if (searchParam != null) {
                    var name = searchParam.value();
                    var argName = "sp_" + name;
                    argNames.add(argName);
                    e.addLine("        var %s = req.searchParams().get(\"%s\", %s.class);", argName, name, param.asType().toString());
                    continue;
                }
            }
        }

        e.addLine("        var result = apis.%s(%s);", method.getSimpleName(), String.join(", ", argNames));
        e.addLine("        res.ok();");
        e.addLine("        res.headers().set(\"Content-Type\", \"application/json\");");
        e.addLine("        res.send(JSON.stringify(result).getBytes());");
        e.addLine("    })");
    }


    private static ApiInfo getApiInfo(ExecutableElement method) {
        var get = method.getAnnotation(GET.class);
        if (get != null) {
            return new ApiInfo("GET", get.value());
        }

        // TODO: more
        throw new RuntimeException("Maybe a bug!");
    }

    private record ApiInfo(String method, String path) {
    }
}
