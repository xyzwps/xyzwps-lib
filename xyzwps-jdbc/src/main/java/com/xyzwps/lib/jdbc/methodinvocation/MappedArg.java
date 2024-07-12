package com.xyzwps.lib.jdbc.methodinvocation;

import com.xyzwps.lib.jdbc.ColumnPropertyMapper;

record MappedArg<T>(T arg, ColumnPropertyMapper<T> mapper) {
}