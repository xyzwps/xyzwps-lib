package com.xyzwps.lib.ap.dsl;

public interface ElementVisitor {

    void visit(ClassElement classElement);

    void visit(FieldElement fieldElement);

    void visit(AnnotationElement annotationElement);
}
