package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class $Field implements Element {
    private final $Type type;
    private final String name;

    private AccessLevel accessLevel = AccessLevel.PRIVATE;
    private boolean isFinal = false;
    private boolean isStatic = false;

    private final List<$Annotation> annotations = new ArrayList<>();

    $Field($Type type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }

    public $Field accessLevel(AccessLevel accessLevel) {
        if (accessLevel != null) {
            this.accessLevel = accessLevel;
        }
        return this;
    }

    public $Field shouldBeStatic() {
        this.isStatic = true;
        return this;
    }

    public $Field shouldBeFinal() {
        this.isFinal = true;
        return this;
    }

    public $Field addAnnotation($Annotation $annotation) {
        this.annotations.add($annotation);
        return this;
    }
}
