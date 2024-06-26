package com.xyzwps.lib.jdbc;

record MappedArg<T>(T arg, ColumnPropertyMapper<T> mapper) {
}