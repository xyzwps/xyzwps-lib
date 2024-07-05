package com.xyzwps.lib.jdbc.method2sql;

public sealed interface Token {
    record Name(String name) implements Token {
    }

    enum KeyWord implements Token {
        FIND,
        GET,
        COUNT,
        UPDATE,
        DELETE,

        BY,
        WHERE,
        AND,
        OR,
        EQ,
        NE,
        GT,
        GE,
        LT,
        LE,
        LIKE,
        NOT,
        IN,
        BETWEEN,
        IS,
        NULL,
        ORDER,
        LIMIT,
        ASC,
        DESC,
        SET
    }
}
