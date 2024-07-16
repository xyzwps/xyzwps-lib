package com.xyzwps.lib.ap.api;

import com.xyzwps.lib.ap.Body;
import com.xyzwps.lib.ap.HeaderParam;
import com.xyzwps.lib.ap.PathParam;
import com.xyzwps.lib.ap.SearchParam;
import com.xyzwps.lib.ap.util.FromTypeMirror;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ApiMethodArguments implements Iterable<ApiMethodArgument> {

    private final List<ApiMethodArgument> arguments = new ArrayList<>();

    private boolean hasBody = false;
    private boolean hasRequest = false;
    private boolean hasResponse = false;

    public void add(ApiMethodArgument arg) {
        switch (arg) {
            case ApiMethodArgument.SearchParam it -> arguments.add(it);
            case ApiMethodArgument.HeaderParam it -> arguments.add(it);
            case ApiMethodArgument.PathParam it -> arguments.add(it);
            case ApiMethodArgument.Body it -> {
                if (hasBody) throw new IllegalStateException("Duplicate body parameter");
                arguments.add(it);
                hasBody = true;
            }
            case ApiMethodArgument.Request it -> {
                if (hasRequest) throw new IllegalStateException("Duplicate request parameter");
                arguments.add(it);
                hasRequest = true;
            }
            case ApiMethodArgument.Response it -> {
                if (hasResponse) throw new IllegalStateException("Duplicate response parameter");
                arguments.add(it);
                hasResponse = true;
            }
        }
    }

    public static ApiMethodArguments from(ExecutableElement method) {
        var arguments = new ApiMethodArguments();
        for (var param : method.getParameters()) {
            var searchParam = param.getAnnotation(SearchParam.class);
            if (searchParam != null) {
                var name = searchParam.value();
                arguments.add(new ApiMethodArgument.SearchParam(name, typeCanonicalName(param.asType())));
                continue;
            }
            var headerParam = param.getAnnotation(HeaderParam.class);
            if (headerParam != null) {
                var name = headerParam.value();
                arguments.add(new ApiMethodArgument.HeaderParam(name, typeCanonicalName(param.asType())));
                continue;
            }
            var pathParam = param.getAnnotation(PathParam.class);
            if (pathParam != null) {
                var name = pathParam.value();
                arguments.add(new ApiMethodArgument.PathParam(name, typeCanonicalName(param.asType())));
                continue;
            }
            var bodyParam = param.getAnnotation(Body.class);
            if (bodyParam != null) {
                arguments.add(new ApiMethodArgument.Body(typeCanonicalName(param.asType())));
                continue;
            }
            if (typeCanonicalName(param.asType()).equals("com.xyzwps.lib.express.HttpRequest")) {
                arguments.add(new ApiMethodArgument.Request());
                continue;
            }
            if (typeCanonicalName(param.asType()).equals("com.xyzwps.lib.express.HttpResponse")) {
                arguments.add(new ApiMethodArgument.Response());
                continue;
            }

            throw new IllegalStateException("Unsupported parameter type: " + typeCanonicalName(param.asType()));
        }
        return arguments;
    }

    private static String typeCanonicalName(TypeMirror mirror) {
        return FromTypeMirror.canonicalName(mirror);
    }

    @Override
    public Iterator<ApiMethodArgument> iterator() {
        return arguments.iterator();
    }
}
