package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class GetImportsVisitor implements ElementVisitor {

    private final Set<String> imports = new HashSet<>();


    @Override
    public void visit(ClassElement e) {
        e.getFields().forEach(this::visit);
        e.getAnnotations().forEach(a -> this.visit(a, true));
        e.getMethods().forEach(this::visit);
        e.getImports().forEach(this::visit);
        e.getImplementedInterfaces().forEach(this::visit);
    }

    @Override
    public void visit(FieldElement e) {
        this.visit(e.getType());
    }

    @Override
    public void visit(AnnotationElement e, boolean inline) {
        this.visit(e.getType());
    }

    public void visit(MethodElement e) {
        var returnType = e.getReturnType();
        if (returnType != null) {
            this.visit(returnType);
        }

        e.getAnnotations().forEach(a -> this.visit(a, true));
        e.getArguments().forEach(this::visit);
    }

    public void visit(FullTypeNameElement e) {
        imports.add(e.getPackageName() + "." + e.getClassName());
    }

    public void visit(ArgumentElement e) {
        this.visit(e.getType());
    }
}
