package com.xyzwps.lib.ap.dsl;

import java.util.ArrayList;
import java.util.List;

public final class ClassElement {

    private final String packageName;
    private final String className;

    private boolean isFinal = false;
    private boolean isPublic = false;

    private final List<FieldElement> fields = new ArrayList<>();

    public ClassElement(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    public ClassElement shouldBeFinal() {
        this.isFinal = true;
        return this;
    }

    public ClassElement shouldBePublic() {
        this.isPublic = true;
        return this;
    }

    public ClassElement addField(FieldElement fieldElement) {
        if (fieldElement != null) {
            this.fields.add(fieldElement);
        }
        return this;
    }
}
