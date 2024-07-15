package com.xyzwps.lib.ap.dsl;

public final class Dsl {

    public static $Annotation $annotation($Type type) {
        return new $Annotation(type);
    }

    public static $Arg $arg($Type type, String name) {
        return new $Arg(type, name);
    }

    public static $Class $class($Type type) {
        return new $Class(type);
    }

    public static $Field $field($Type type, String name) {
        return new $Field(type, name);
    }

    public static $Method $method($Type returnType, String name) {
        return new $Method(returnType, name);
    }

    public static $Type $type(String packageName, String className) {
        return new $Type(packageName, className);
    }
}
