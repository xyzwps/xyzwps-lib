package com.xyzwps.lib.ap.dsl;

public interface ElementVisitor {

    void visit(ClassElement clazz);

    void visit(FieldElement field);

    void visit(AnnotationElement annotation);

    void visit(MethodElement method);

    void visit(FullTypeNameElement fullTypeName);
}
