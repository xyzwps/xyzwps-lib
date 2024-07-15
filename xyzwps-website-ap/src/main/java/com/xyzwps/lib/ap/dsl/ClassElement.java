package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public final class ClassElement implements Element {
    private final FullTypeNameElement type;

    private boolean isFinal = false;
    private boolean isPublic = false;

    private final Map<String, FieldElement> fields = new HashMap<>();
    private final List<AnnotationElement> annotations = new ArrayList<>();
    private final List<MethodElement> methods = new ArrayList<>();

    private final List<FullTypeNameElement> implementedInterfaces = new ArrayList<>();

    private final List<FullTypeNameElement> imports = new ArrayList<>();

    public ClassElement(FullTypeNameElement type) {
        this.type = type;
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
            this.fields.put(fieldElement.getName(), fieldElement);
        }
        return this;
    }

    public ClassElement addMethod(MethodElement methodElement) {
        if (methodElement != null) {
            this.methods.add(methodElement);
        }
        return this;
    }

    public ClassElement addAnnotation(AnnotationElement annotationElement) {
        if (annotationElement != null) {
            this.annotations.add(annotationElement);
        }
        return this;
    }

    public ClassElement addImplementedInterface(FullTypeNameElement fullTypeNameElement) {
        if (fullTypeNameElement != null) {
            this.implementedInterfaces.add(fullTypeNameElement);
        }
        return this;
    }

    public ClassElement addImport(FullTypeNameElement fullTypeNameElement) {
        if (fullTypeNameElement != null) {
            this.imports.add(fullTypeNameElement);
        }
        return this;
    }
}
