package com.xyzwps.lib.ap.util;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;

public final class FromTypeMirror {

    public static String canonicalName(TypeMirror type) {
        return switch (type) {
            case IntersectionType ignored ->
                    throw new UnsupportedOperationException("Intersection types are not supported yet");
            case UnionType ignored -> throw new UnsupportedOperationException("Union types are not supported yet");
            case ExecutableType ignored ->
                    throw new UnsupportedOperationException("Executable types are not supported yet");
            case NoType ignored -> throw new UnsupportedOperationException("NoType is not supported yet");
            case PrimitiveType ignored -> switch (type.getKind()) {
                case BOOLEAN -> "boolean";
                case BYTE -> "byte";
                case SHORT -> "short";
                case INT -> "int";
                case LONG -> "long";
                case CHAR -> "char";
                case FLOAT -> "float";
                case DOUBLE -> "double";
                default -> throw new UnsupportedOperationException("Unknown primitive type: " + type);
            };
            case WildcardType ignored ->
                    throw new UnsupportedOperationException("Wildcard types are not supported yet");
            case ArrayType ignored -> throw new UnsupportedOperationException("Array types are not supported yet");
            case NullType ignored -> throw new UnsupportedOperationException("Null types are not supported yet");
            case DeclaredType t -> {
                var element = t.asElement();
                if (element instanceof TypeElement te) {
                    yield te.getQualifiedName().toString();
                }
                throw new UnsupportedOperationException("DeclaredType types are not supported yet");
            }
            case TypeVariable ignored ->
                    throw new UnsupportedOperationException("Type variables are not supported yet");
            default -> throw new UnsupportedOperationException("Unknown type: " + type);
        };
    }
}
