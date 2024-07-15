package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public final class $Class implements Element {
    private final $Type type;

    private boolean isFinal = false;
    private AccessLevel accessLevel = AccessLevel.PUBLIC;

    private final Map<String, $Field> fields = new HashMap<>();
    private final List<$Annotation> annotations = new ArrayList<>();
    private final List<$Method> methods = new ArrayList<>();

    private final List<$Type> implementedInterfaces = new ArrayList<>();

    private final List<$Type> imports = new ArrayList<>();

    $Class($Type type) {
        this.type = type;
    }

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }

    public $Class useFinal() {
        this.isFinal = true;
        return this;
    }

    public $Class usePublic() {
        this.accessLevel = AccessLevel.PUBLIC;
        return this;
    }

    public List<$Field> getFields() {
        return new ArrayList<>(fields.values());
    }

    public $Class addField($Field $field) {
        if ($field != null) {
            this.fields.put($field.getName(), $field);
        }
        return this;
    }

    public $Class addMethod($Method $method) {
        if ($method != null) {
            this.methods.add($method);
        }
        return this;
    }

    public $Class doAnnotate($Annotation $annotation) {
        if ($annotation != null) {
            this.annotations.add($annotation);
        }
        return this;
    }

    public $Class doImplement($Type $Type) {
        if ($Type != null) {
            this.implementedInterfaces.add($Type);
        }
        return this;
    }

    public $Class doImport($Type $Type) {
        if ($Type != null) {
            this.imports.add($Type);
        }
        return this;
    }
}
