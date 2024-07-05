package com.xyzwps.lib.jdbc.method2sql;

public final class MethodName2Sql {

    public static SqlInfo getSql(String methodName, String tableName) {
        var tokens = Tokenizer.tokenize(methodName);
        var toAst = new Tokens2Ast(tokens);
        var ast =  toAst.parse();
        if (!toAst.eof()) {
            throw new IllegalStateException("Unexpected token");
        }
        var visitor = new AstNode2SqlVisitor(tableName);
        ast.visit(visitor);
        return visitor.toSql();
    }
}
