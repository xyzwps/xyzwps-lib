package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class $Arg implements Element {

    private final $Type type;
    private final String name;

    private final List<$Annotation> annotations = new ArrayList<>();

    $Arg($Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public $Arg addAnnotation($Annotation $annotation) {
        annotations.add($annotation);
        return this;
    }

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
