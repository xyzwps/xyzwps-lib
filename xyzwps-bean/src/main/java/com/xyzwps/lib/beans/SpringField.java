package com.xyzwps.lib.beans;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * TODO: 重命名
 */
public class SpringField {

    private final String fieldName;
    private final Field field;
    private final AccessLevel accessLevel;
    private final Class<?> beanType;
    private final Class<?> fieldType;
    private final boolean isFinal;

    private SpringField(Class<?> beanType, Field field) {
        this.beanType = Objects.requireNonNull(beanType);
        this.field = Objects.requireNonNull(field);
        this.fieldName = field.getName();
        this.fieldType = field.getType();
        var modifiers = field.getModifiers();
        this.accessLevel = AccessLevel.fromModifiers(modifiers);
        this.isFinal = Modifier.isFinal(modifiers);
    }

    public boolean readable() {
        return accessLevel.readable;
    }

    public GetResult getValue(Object object) {
        try {
            return GetResult.ok(this.field.get(object));
        } catch (Exception e) {
            return GetResult.failed(e);
        }
    }

    public SetResult setValue(Object object, Object value) {
        try {
            this.field.set(object, value);
            return SetResult.OK;
        } catch (Exception e) {
            return SetResult.failed(e);
        }
    }

    public Field getField() {
        return field;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Optional<SpringField> create(Class<?> beanType, Field field) {
        if (Modifier.isStatic(field.getModifiers())) {
            return Optional.empty();
        }
        if (propertyFieldPattern.matcher(field.getName()).matches()) {
            return Optional.of(new SpringField(beanType, field));
        }
        return Optional.empty();
    }

    static Pattern propertyFieldPattern = Pattern.compile("^[a-z][A-Za-z0-9]*");
}