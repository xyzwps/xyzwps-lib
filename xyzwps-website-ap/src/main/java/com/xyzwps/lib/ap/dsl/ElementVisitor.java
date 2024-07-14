package com.xyzwps.lib.ap.dsl;

public interface ElementVisitor {

    void visit(ClassElement clazz);

    void visit(FieldElement field);

    // TODO: 这个 api 有点奇怪，需要重构
    void visit(AnnotationElement annotation, boolean inline);

    void visit(MethodElement method);

    void visit(FullTypeNameElement fullTypeName);

    void visit(ArgumentElement argument);
}
