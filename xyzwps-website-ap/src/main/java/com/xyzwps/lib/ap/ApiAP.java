package com.xyzwps.lib.ap;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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
                            .toList();
                    if (!methods.isEmpty()) {
                        generateRouterClass(typeElement, methods);
                        writeJavaFile();
                    }
                }
            }
        }
        return true;
    }

    private static void writeJavaFile() {
        // TODO:
    }

    private static void generateRouterClass(TypeElement typeElement, List<ExecutableElement> methods) {
        var daoClassName = typeElement.getQualifiedName().toString();
        var i = daoClassName.lastIndexOf(".");
        var packageName = daoClassName.substring(0, i);
        var simpleName = daoClassName.substring(i + 1);

        var apiPrefix = typeElement.getAnnotation(API.class).value();

        var generatedClassName = simpleName + "RouterAP";

        var sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";\n");
        sb.append('\n');
        sb.append("import ").append(daoClassName).append(";\n");
        sb.append("import com.xyzwps.lib.express.filter.Router;\n");
        sb.append("import com.xyzwps.lib.express.HttpMethod;\n");
        sb.append("import com.xyzwps.website.common.JSON;\n");
        sb.append("import jakarta.inject.Singleton;\n");
        sb.append("\n");
        sb.append("@Singleton\n");
        sb.append("public class ").append(generatedClassName).append(" {\n");
        sb.append("\n");
        sb.append("    private final ").append(simpleName).append(" apis;\n");
        sb.append("\n");
        sb.append("    public ").append(generatedClassName).append("(").append(simpleName).append(" apis) {\n");
        sb.append("        this.apis = apis;\n");
        sb.append("    }\n");
        sb.append("\n");
        sb.append("    public void register(Router router) {\n");
        sb.append("        router\n");
        for (var method : methods) {
            var apiInfo = getApiInfo(method);
            sb.append("    ");
            sb.append("        .handle(HttpMethod.").append(apiInfo.method).append(", ")
                    .append("\"").append(apiPrefix).append(apiInfo.path).append("\", ")
                    .append("(req, res, next) -> {\n");
            handleMethod(method, sb);
            sb.append("            })\n");
        }
        sb.append("        ;\n");
        sb.append("    }\n");

        sb.append('}');

        System.out.println(sb);
    }

    private static final String TAB4 = "                ";


    private static void handleMethod(ExecutableElement method, StringBuilder sb) {
        var params = method.getParameters();
        var argNames = new ArrayList<String>();
        for (var param : params) {
            {
                var searchParam = param.getAnnotation(SearchParam.class);
                if (searchParam != null) {
                    var name = searchParam.value();
                    var argName = "sp_" + name;
                    argNames.add(argName);
                    sb.append(TAB4)
                            .append("var ").append(argName).append(" = req.searchParams().get(\"").append(name).append("\", ")
                            .append(param.asType().toString()).append(".class);\n");
                    continue;
                }
            }
        }

        // TODO: handle void
        sb.append(TAB4).append("var result = apis.").append(method.getSimpleName()).append("(")
                .append(String.join(", ", argNames)).append(");\n");
        sb.append(TAB4).append("res.ok();\n");
        sb.append(TAB4).append("res.headers().set(\"Content-Type\", \"application/json\");\n");
        sb.append(TAB4).append("res.send(JSON.stringify(result).getBytes());\n");
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
