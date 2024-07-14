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

    private AccessLevel accessLevel = AccessLevel.PUBLIC;

    private final List<String> lines = new ArrayList<>();

    private boolean isStatic = false;

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

    public MethodElement accessLevel(AccessLevel accessLevel) {
        if (accessLevel != null) {
            this.accessLevel = accessLevel;
        }
        return this;
    }

    public MethodElement isStatic(boolean isStatic) {
        this.isStatic = isStatic;
        return this;
    }

    public MethodElement addLine(String line, Object... args) {
        if (line != null) {
            this.lines.add(String.format(line, args));
        }
        return this;
    }

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
