package com.xyzwps.lib.jdbc.method2sql;

import static com.xyzwps.lib.jdbc.method2sql.AstNode.*;

public interface AstNodeVisitor {

    void visit(FindMethod findMethod);

    void visit(CountMethod countMethod);

    void visit(UpdateMethod updateMethod);

    void visit(DeleteMethod deleteMethod);

    void visit(Condition condition);

    void visit(EqExp eqExp);

    void visit(NeExp neExp);

    void visit(GtExp gtExp);

    void visit(GeExp geExp);

    void visit(LeExp leExp);

    void visit(LtExp ltExp);

    void visit(LikeExp likeExp);

    void visit(NotLikeExp notLikeExp);

    void visit(IsNullExp isNullExp);

    void visit(IsNotNullExp isNotNullExp);

    void visit(BetweenAndExp betweenExp);

    void visit(NotBetweenAndExp notBetweenExp);

    void visit(InExp inExp);

    void visit(NotInExp notInExp);

    void visit(AndExp andExp);

    void visit(OrExp orExp);

    void visit(Column column);

    void visit(ColumnNameSegment columnNameSegment);

    void visit(SetPart setPart);

    void visit(OrderByPart orderByPart);

    void visit(LimitPart limitPart);

    void visit(OrderByList orderByList);

    void visit(OrderByColumn orderByColumn);

    void visit(SetList setList);
}
