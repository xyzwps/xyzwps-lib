package com.xyzwps.lib.ap.dsl;

public interface ElementVisitor {

    void visit($Class clazz);

    void visit($Field field);

    void visit($Annotation annotation);

    void visit($Method method);

    void visit($Type fullTypeName);

    void visit($Arg argument);
}
