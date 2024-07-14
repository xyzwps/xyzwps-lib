package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class MethodElement implements Element {

    private final FullTypeNameElement returnType;
    private final String name;
    private final List<AnnotationElement> annotations = new ArrayList<>();
    private final List<ArgumentElement> arguments = new ArrayList<>();

    // TODO: 重构 modifier

    public MethodElement(FullTypeNameElement returnType, String name) {
        this.returnType = returnType;
        this.name = name;
    }

    public MethodElement addAnnotation(AnnotationElement annotationElement) {
        if (annotationElement != null) {
            this.annotations.add(annotationElement);
        }
        return this;
    }

    public MethodElement addArgument(ArgumentElement argumentElement) {
        if (argumentElement != null) {
            this.arguments.add(argumentElement);
        }
        return this;
    }

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
