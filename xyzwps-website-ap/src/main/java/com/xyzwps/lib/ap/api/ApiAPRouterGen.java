package com.xyzwps.lib.ap.api;

import com.xyzwps.lib.ap.dsl.$Class;
import com.xyzwps.lib.ap.dsl.$Method;
import com.xyzwps.lib.ap.dsl.$Type;
import com.xyzwps.lib.ap.dsl.ToJavaClassVisitor;
import com.xyzwps.lib.ap.util.CanonicalName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.xyzwps.lib.ap.dsl.AccessLevel.PRIVATE;
import static com.xyzwps.lib.ap.dsl.Dsl.*;
import static com.xyzwps.lib.ap.dsl.Dsl.$field;

public final class ApiAPRouterGen extends ApiAPGen {

    public ApiAPRouterGen(TypeElement typeElement) {
        super(typeElement);
    }

    public void generate(BiConsumer<$Type, String> writeJavaFile) {
        var methods = collectApiMethods();
        if (!methods.isEmpty()) {
            var source = generateRouterClass(methods);
            writeJavaFile.accept(source.getKey(), source.getValue());
        }
    }

    private Map.Entry<$Type, String> generateRouterClass(List<ExecutableElement> methods) {
        var daoClassName = CanonicalName.of(apiTypeElement.getQualifiedName().toString());
        var generatedClassName = daoClassName.className() + "Router$AP";
        var generatedClassType = $type(daoClassName.packageName(), generatedClassName);
        var apiClassType = $type(daoClassName.packageName(), daoClassName.className());
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


    private static String classNameToVarName(String className) {
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    private static void handleMethod($Class $class, ExecutableElement method, String apiPrefix, $Method e) {
        var apiInfo = ApiInfo.from(method);

        var filterFieldNames = new ArrayList<String>();
        apiInfo.filters().forEach(it -> {
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

        e.addLine("    .%s(\"%s\", %s (req, res) -> {", apiInfo.method(), apiPrefix + apiInfo.path(), filterPart);

        var argNames = new ArrayList<String>();
        apiInfo.arguments().forEach(arg -> {
            switch (arg) {
                case ApiMethodArgument.SearchParam it -> {
                    var argName = "sp_" + it.name();
                    argNames.add(argName);
                    e.addLine("        var %s = req.searchParams().get(\"%s\", %s.class);", argName, it.name(), it.type());
                }
                case ApiMethodArgument.HeaderParam it -> {
                    var argName = "hp_" + it.name();
                    argNames.add(argName);
                    e.addLine("        var %s = req.header(\"%s\", %s.class);", argName, it.name(), it.type());
                }
                case ApiMethodArgument.PathParam it -> {
                    var argName = "pp_" + it.name();
                    argNames.add(argName);
                    e.addLine("        var %s = req.pathVariables().get(\"%s\", %s.class);", argName, it.name(), it.type());
                }
                case ApiMethodArgument.Body it -> {
                    var argName = "body";
                    argNames.add(argName);
                    e.addLine("        var %s = req.json(%s.class, JSON.JM);", argName, it.type());
                }
                case ApiMethodArgument.Request ignored -> argNames.add("req");
                case ApiMethodArgument.Response ignored -> argNames.add("res");
            }
        });

        e.addLine("        var result = apis.%s(%s);", method.getSimpleName(), String.join(", ", argNames));
        e.addLine("        res.ok();");
        e.addLine("        res.headers().set(\"Content-Type\", \"application/json\");");
        e.addLine("        res.send(JSON.stringify(result).getBytes());");
        e.addLine("    })");
    }
}
