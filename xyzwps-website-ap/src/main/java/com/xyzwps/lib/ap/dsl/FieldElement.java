package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class FieldElement implements Element {
    private final String typePackageName;
    private final String typeClassName;
    private final String name;

    private boolean isPrivate = false;
    private boolean isFinal = false;
    private boolean isStatic = false;

    private final List<AnnotationElement> annotations = new ArrayList<>();

    public FieldElement(String typePackageName, String typeClassName, String name) {
        this.typePackageName = typePackageName;
        this.typeClassName = typeClassName;
        this.name = name;
    }

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }

    public FieldElement shouldBePrivate() {
        this.isPrivate = true;
        return this;
    }

    public FieldElement shouldBeStatic() {
        this.isStatic = true;
        return this;
    }

    public FieldElement shouldBeFinal() {
        this.isFinal = true;
        return this;
    }

    public FieldElement addAnnotation(AnnotationElement annotationElement) {
        this.annotations.add(annotationElement);
        return this;
    }
}
