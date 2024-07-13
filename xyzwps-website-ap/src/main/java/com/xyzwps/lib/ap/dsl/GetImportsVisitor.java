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
        e.getAnnotations().forEach(this::visit);
    }

    @Override
    public void visit(FieldElement e) {
        imports.add(e.getTypePackageName() + "." + e.getTypeClassName());
    }

    @Override
    public void visit(AnnotationElement e) {
        imports.add(e.getAnnoPackageName() + "." + e.getAnnoClassName());
    }
}
