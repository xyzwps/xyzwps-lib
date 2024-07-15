package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class GetImportsVisitor implements ElementVisitor {

    private final Set<String> imports = new HashSet<>();


    @Override
    public void visit($Class e) {
        e.getFields().forEach(f -> f.visit(this));
        e.getAnnotations().forEach(a -> a.visit(this));
        e.getMethods().forEach(it -> it.visit(this));
        e.getImports().forEach(it -> it.visit(this));
        e.getImplementedInterfaces().forEach(it -> it.visit(this));
    }

    @Override
    public void visit($Field e) {
        e.getType().visit(this);
    }

    @Override
    public void visit($Annotation e) {
        e.getType().visit(this);
    }

    public void visit($Method e) {
        var returnType = e.getReturnType();
        if (returnType != null) {
            this.visit(returnType);
        }

        e.getAnnotations().forEach(a -> a.visit(this));
        e.getArguments().forEach(this::visit);
    }

    public void visit($Type e) {
        imports.add(e.packageName() + "." + e.className());
    }

    public void visit($Arg e) {
        this.visit(e.getType());
    }
}
