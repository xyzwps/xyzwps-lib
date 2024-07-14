package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class FieldElement implements Element {
    private final FullTypeNameElement type;
    private final String name;

    private AccessLevel accessLevel = AccessLevel.PRIVATE;
    private boolean isFinal = false;
    private boolean isStatic = false;

    private final List<AnnotationElement> annotations = new ArrayList<>();

    public FieldElement(FullTypeNameElement type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }

    public FieldElement accessLevel(AccessLevel accessLevel) {
        if (accessLevel != null) {
            this.accessLevel = accessLevel;
        }
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
