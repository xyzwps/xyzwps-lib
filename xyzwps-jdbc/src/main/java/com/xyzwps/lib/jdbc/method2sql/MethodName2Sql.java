package com.xyzwps.lib.jdbc.method2sql;

import java.util.concurrent.ConcurrentHashMap;

public final class MethodName2Sql {

    private static final ConcurrentHashMap<String, SqlInfo> CACHE = new ConcurrentHashMap<>();

    public static SqlInfo getSql(String methodName, String tableName) {
        var key = methodName + "@" + tableName;
        return CACHE.computeIfAbsent(key, k -> {
            var tokens = Tokenizer.tokenize(methodName);
            var toAst = new Tokens2Ast(tokens);
            var ast = toAst.parse();
            if (!toAst.eof()) {
                throw new IllegalStateException("Unexpected token");
            }
            var visitor = new AstNode2SqlVisitor(tableName);
            ast.visit(visitor);
            return visitor.toSql();
        });
    }
}
