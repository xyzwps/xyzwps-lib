package com.xyzwps.lib.ap.api;

import com.xyzwps.lib.ap.API;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.stream.Stream;

public abstract class ApiAPGen {

    protected final String apiPrefix;
    protected final String apiClassName;
    protected final TypeElement apiTypeElement;

    protected ApiAPGen(TypeElement typeElement) {
        this.apiTypeElement = typeElement;
        this.apiPrefix = typeElement.getAnnotation(API.class).value();
        this.apiClassName = typeElement.getSimpleName().toString();
    }


    protected List<ExecutableElement> collectApiMethods() {
        return apiTypeElement.getEnclosedElements()
                .stream()
                .filter(this::method_should_be_public_and_not_static_and_not_abstract)
                .flatMap(this::flat_to_methods)
                .filter(this::exclude_constructor)
                .toList();
    }


    private boolean exclude_constructor(ExecutableElement method) {
        return !method.getSimpleName().toString().equals("<init>");
    }

    private Stream<ExecutableElement> flat_to_methods(Element element) {
        return element instanceof ExecutableElement method ? Stream.of(method) : Stream.empty();
    }

    private boolean method_should_be_public_and_not_static_and_not_abstract(Element method) {
        var modifiers = method.getModifiers();
        return modifiers.contains(Modifier.PUBLIC)
               && !modifiers.contains(Modifier.STATIC)
               && !modifiers.contains(Modifier.ABSTRACT);
    }
}
