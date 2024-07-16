package com.xyzwps.lib.openapi;

public sealed interface DataType {

    enum Null implements DataType {
        INSTANCE
    }

    enum Boolean implements DataType {
        INSTANCE
    }

    enum Integer implements DataType {
        INT32, INT64
    }

    enum Number implements DataType {
        FLOAT, DOUBLE
    }

    enum String implements DataType {
        BINARY, DATE, DATE_TIME, PASSWORD
    }

}
