package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class $Method implements Element {

    private final $Type returnType;
    private final String name;
    private final List<$Annotation> annotations = new ArrayList<>();
    private final List<$Arg> arguments = new ArrayList<>();

    private AccessLevel accessLevel = AccessLevel.PUBLIC;

    private final List<String> lines = new ArrayList<>();

    private boolean isStatic = false;

    $Method($Type returnType, String name) {
        this.returnType = returnType;
        this.name = name;
    }

    public $Method addAnnotation($Annotation $annotation) {
        if ($annotation != null) {
            this.annotations.add($annotation);
        }
        return this;
    }

    public $Method addArgument($Arg $Arg) {
        if ($Arg != null) {
            this.arguments.add($Arg);
        }
        return this;
    }

    public $Method accessLevel(AccessLevel accessLevel) {
        if (accessLevel != null) {
            this.accessLevel = accessLevel;
        }
        return this;
    }

    public $Method isStatic(boolean isStatic) {
        this.isStatic = isStatic;
        return this;
    }

    public $Method addLine(String line, Object... args) {
        if (line != null) {
            this.lines.add(String.format(line, args));
        }
        return this;
    }

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
