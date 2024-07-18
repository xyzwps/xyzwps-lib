package com.xyzwps.lib.ap.api;

import com.xyzwps.lib.ap.util.CanonicalName;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;

public class ApiAPOpenapiGen extends ApiAPGen {

    public ApiAPOpenapiGen(TypeElement typeElement) {
        super(typeElement);
    }

    public void generate(ProcessingEnvironment processingEnv) {
        var apiClassName = CanonicalName.of(apiTypeElement.getQualifiedName().toString());
        var className = apiClassName.className() + "OpenApi$AP";
        var packageName = apiClassName.packageName();

        var methods = collectApiMethods();
        var lines = new Lines();
        lines.add("package " + packageName + ";").add(null);

        lines.add("import com.xyzwps.lib.openapi.Document;");
        lines.add("import com.xyzwps.lib.openapi.Info;");
        lines.add("import com.xyzwps.lib.openapi.Api;");
        lines.add(null);

        lines.add("public class " + className + " {").indentPP().add(null);

        lines.add("public static Document generate(Document doc) {").indentPP().add(null);

        int i = 1;
        for (var method : methods) {
            var apiInfo = ApiInfo.from(method);
            lines.add("// " + i++);
            lines.add("{").indentPP();

            lines.add("var api = new Api(\"" + apiInfo.method().toLowerCase() + "\", \"" + apiInfo.path() + "\");");

            for (var arg : apiInfo.arguments()) {
                switch (arg) {
                    case ApiMethodArgument.SearchParam it ->
                            lines.add("api.addSearchParam(\"" + it.name() + "\", " + it.type() + ".class);");
                    case ApiMethodArgument.HeaderParam it ->
                            lines.add("api.addHeaderParam(\"" + it.name() + "\", " + it.type() + ".class);");
                    case ApiMethodArgument.PathParam it ->
                            lines.add("api.addPathParam(\"" + it.name() + "\", " + it.type() + ".class);");
                    case ApiMethodArgument.Body it -> lines.add("api.addRequestBody(" + it.type() + ".class);");
                    default -> System.out.println();
                }
            }
            lines.add("doc.addApi(api);");
            lines.indentMM().add("}")
                    .add("");
        }

        lines.add("return doc;");
        lines.indentMM().add("} // end generate").add(null);

        lines.indentMM().add("} // end class");

        writeJavaFile(packageName + "." + className, lines.toString(), processingEnv);
    }

    private static class Lines {
        private final List<String> text = new ArrayList<>();
        private final List<Integer> indents = new ArrayList<>();

        private int indent = 0;

        Lines add(String line) {
            text.add(line);
            indents.add(indent);
            return this;
        }

        Lines indentPP() {
            indent++;
            return this;
        }

        Lines indentMM() {
            indent--;
            return this;
        }

        @Override
        public String toString() {
            var sb = new StringBuilder();
            for (int i = 0; i < text.size(); i++) {
                var it = text.get(i);
                if (it == null) {
                    sb.append("\n");
                } else {
                    sb.append("    ".repeat(Math.max(0, indents.get(i)))).append(it).append("\n");
                }
            }
            return sb.toString();
        }
    }
}
