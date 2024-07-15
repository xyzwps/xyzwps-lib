package com.xyzwps.lib.ap;

import com.google.auto.service.AutoService;
import com.xyzwps.lib.ap.dsl.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypesException;
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
import static com.xyzwps.lib.ap.dsl.Dsl.*;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.xyzwps.lib.ap.API")
public class ApiAP extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            for (TypeElement annotation : annotations) {
                for (var element : roundEnv.getElementsAnnotatedWith(annotation)) {
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

    private static Map.Entry<$Type, String> generateRouterClass(TypeElement typeElement, List<ExecutableElement> methods) {
        var daoClassName = typeElement.getQualifiedName().toString();
        var i = daoClassName.lastIndexOf(".");
        var packageName = daoClassName.substring(0, i);
        var simpleName = daoClassName.substring(i + 1);

        var apiPrefix = typeElement.getAnnotation(API.class).value();

        var generatedClassName = simpleName + "Router$AP";

        var generatedClassType = $type(packageName, generatedClassName);
        var apiClassType = $type(packageName, simpleName);
        var annoSingleton = $type("jakarta.inject", "Singleton");
        var routerType = $type("com.xyzwps.lib.express.filter", "Router");
        var httpMethodType = $type("com.xyzwps.lib.express", "HttpMethod");
        var httpRequestType = $type("com.xyzwps.lib.express", "HttpRequest");
        var httpResponseType = $type("com.xyzwps.lib.express", "HttpResponse");
        var jsonType = $type("com.xyzwps.website.common", "JSON");
        var routerMakerType = $type("com.xyzwps.website.filter", "RouterMaker");

        var generatedClass = $class(generatedClassType)
                .usePublic()
                .doAnnotate($annotation(annoSingleton))
                .doImplement(routerMakerType);

        var buildApisMethod = $method(null, "make")
                .addArgument($arg(routerType, "router"))
                .addLine("router");
        handleMethods(generatedClass, methods, apiPrefix, buildApisMethod);
        buildApisMethod.addLine(";");

        generatedClass
                .addField($field(apiClassType, "apis").accessLevel(PRIVATE).shouldBeFinal())
                .doImport(httpMethodType)
                .doImport(jsonType)
                .doImport(httpRequestType)
                .doImport(httpResponseType)
                .addMethod(buildApisMethod);
        var toJavaClass = new ToJavaClassVisitor();
        generatedClass.visit(toJavaClass);
        return Map.entry(generatedClassType, toJavaClass.toJavaClass());
    }

    private static void handleMethods($Class $class, List<ExecutableElement> executableElements, String apiPrefix, $Method $method) {
        for (var method : executableElements) {
            handleMethod($class, method, apiPrefix, $method);
        }
    }

    private static void handleMethod($Class $class, ExecutableElement method, String apiPrefix, $Method e) {
        var apiInfo = getApiInfo(method);

        var filterFieldNames = new ArrayList<String>();
        apiInfo.filters.forEach(it -> {
            var i = it.lastIndexOf(".");
            var packageName = it.substring(0, i);
            var className = it.substring(i + 1);
            var filterType = $type(packageName, className);
            var filterFieldName = classNameToVarName(className);
            $class.addField($field(filterType, filterFieldName).accessLevel(PRIVATE).shouldBeFinal());
            filterFieldNames.add(filterFieldName);
        });

        var filterPart = new StringBuilder();
        for (int i = 0; i < filterFieldNames.size(); i++) {
            var name = filterFieldNames.get(i);
            if (i == 0) {
                filterPart.append(name);
            } else {
                filterPart.append(".andThen(").append(name).append(")");
            }
        }
        if (!filterFieldNames.isEmpty()) {
            filterPart.append(",");
        }

        e.addLine("    .%s(\"%s\", %s (req, res) -> {", apiInfo.method, apiPrefix + apiInfo.path, filterPart);

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
            {
                var headerParam = param.getAnnotation(HeaderParam.class);
                if (headerParam != null) {
                    var name = headerParam.value();
                    var argName = "hp_" + name;
                    argNames.add(argName);
                    e.addLine("        var %s = req.header(\"%s\", %s.class);", argName, name, param.asType().toString());
                    continue;
                }
            }
            {
                var pathParam = param.getAnnotation(PathParam.class);
                if (pathParam != null) {
                    var name = pathParam.value();
                    var argName = "pp_" + name;
                    argNames.add(argName);
                    e.addLine("        var %s = req.pathVariables().get(\"%s\", %s.class);", argName, name, param.asType().toString());
                    continue;
                }
            }
            {
                var bodyParam = param.getAnnotation(Body.class);
                if (bodyParam != null) {
                    var argName = "body";
                    argNames.add(argName);
                    e.addLine("        var %s = req.json(%s.class, JSON.JM);", argName, param.asType().toString());
                    continue;
                }
            }
            {
                if (param.asType().toString().equals("com.xyzwps.lib.express.HttpRequest")) {
                    var argName = "req";
                    argNames.add(argName);
                    continue;
                }
            }
            {
                if (param.asType().toString().equals("com.xyzwps.lib.express.HttpResponse")) {
                    var argName = "res";
                    argNames.add(argName);
                    continue;
                }
            }

            throw new IllegalStateException("Unsupported parameter type: " + param.asType().toString());
        }

        e.addLine("        var result = apis.%s(%s);", method.getSimpleName(), String.join(", ", argNames));
        e.addLine("        res.ok();");
        e.addLine("        res.headers().set(\"Content-Type\", \"application/json\");");
        e.addLine("        res.send(JSON.stringify(result).getBytes());");
        e.addLine("    })");
    }


    private static ApiInfo getApiInfo(ExecutableElement method) {
        List<String> filters = new ArrayList<>();

        var get = method.getAnnotation(GET.class);
        if (get != null) {
            try {
                for (var filter : get.filters()) {
                    filters.add(filter.getCanonicalName());
                }
            } catch (MirroredTypesException e) {
                for (var type : e.getTypeMirrors()) {
                    filters.add(type.toString());
                }
            }
            return new ApiInfo("get", get.value(), filters);
        }

        var post = method.getAnnotation(POST.class);
        if (post != null) {
            try {
                for (var filter : post.filters()) {
                    filters.add(filter.getCanonicalName());
                }
            } catch (MirroredTypesException e) {
                for (var type : e.getTypeMirrors()) {
                    filters.add(type.toString());
                }
            }
            return new ApiInfo("post", post.value(), filters);
        }

        var put = method.getAnnotation(PUT.class);
        if (put != null) {
            try {
                for (var filter : put.filters()) {
                    filters.add(filter.getCanonicalName());
                }
            } catch (MirroredTypesException e) {
                for (var type : e.getTypeMirrors()) {
                    filters.add(type.toString());
                }
            }
            return new ApiInfo("put", put.value(), filters);
        }

        var delete = method.getAnnotation(DELETE.class);
        if (delete != null) {
            try {
                for (var filter : delete.filters()) {
                    filters.add(filter.getCanonicalName());
                }
            } catch (MirroredTypesException e) {
                for (var type : e.getTypeMirrors()) {
                    filters.add(type.toString());
                }
            }
            return new ApiInfo("delete", delete.value(), filters);
        }

        var patch = method.getAnnotation(PATCH.class);
        if (patch != null) {
            try {
                for (var filter : patch.filters()) {
                    filters.add(filter.getCanonicalName());
                }
            } catch (MirroredTypesException e) {
                for (var type : e.getTypeMirrors()) {
                    filters.add(type.toString());
                }
            }
            return new ApiInfo("patch", patch.value(), filters);
        }

        // TODO: more methods
        throw new RuntimeException("Maybe a bug!");
    }

    private record ApiInfo(String method, String path, List<String> filters) {
    }


    private static String classNameToVarName(String className) {
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

}
