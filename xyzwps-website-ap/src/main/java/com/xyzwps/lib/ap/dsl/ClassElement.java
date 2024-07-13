package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class ClassElement implements Element {
    private final String packageName;
    private final String className;

    private boolean isFinal = false;
    private boolean isPublic = false;

    private final List<FieldElement> fields = new ArrayList<>();
    private final List<AnnotationElement> annotations = new ArrayList<>();

    public ClassElement(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }

    public ClassElement shouldBeFinal() {
        this.isFinal = true;
        return this;
    }

    public ClassElement shouldBePublic() {
        this.isPublic = true;
        return this;
    }

    public ClassElement addField(FieldElement fieldElement) {
        if (fieldElement != null) {
            this.fields.add(fieldElement);
        }
        return this;
    }

    public ClassElement addAnnotation(AnnotationElement annotationElement) {
        if (annotationElement != null) {
            this.annotations.add(annotationElement);
        }
        return this;
    }
}
