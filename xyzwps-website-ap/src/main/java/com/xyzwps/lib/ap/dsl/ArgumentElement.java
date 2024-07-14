package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ArgumentElement implements Element {

    private final FullTypeNameElement type;
    private final String name;

    private final List<AnnotationElement> annotations = new ArrayList<>();

    public ArgumentElement(FullTypeNameElement type, String name) {
        this.type = type;
        this.name = name;
    }

    public ArgumentElement addAnnotation(AnnotationElement annotationElement) {
        annotations.add(annotationElement);
        return this;
    }

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
