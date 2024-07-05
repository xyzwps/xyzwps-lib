package com.xyzwps.lib.jdbc.method2sql;

// TODO: 生成的 sql 和具体数据库有关
class AstNode2SqlVisitor implements AstNodeVisitor {

    private final String tableName;

    private final StringBuilder sb = new StringBuilder();

    private final BooleanList placeholderIsIn = new BooleanList();

    private SqlType sqlType = null;

    AstNode2SqlVisitor(String tableName) {
        this.tableName = tableName;
    }

    public SqlInfo toSql() {
        return new SqlInfo(sb.toString(), placeholderIsIn, sqlType);
    }

    @Override
    public void visit(AstNode.FindMethod findMethod) {
        sqlType = SqlType.SELECT;
        sb.append("SELECT * FROM ").append(tableName);
        if (findMethod.condition() != null) {
            findMethod.condition().visit(this);
        }
        if (findMethod.orderBy() != null) {
            findMethod.orderBy().visit(this);
        }
        if (findMethod.limit() != null) {
            findMethod.limit().visit(this);
        }
    }

    @Override
    public void visit(AstNode.CountMethod countMethod) {
        sqlType = SqlType.COUNT;
        sb.append("SELECT COUNT(*) FROM ").append(tableName);
        if (countMethod.condition() != null) {
            countMethod.condition().visit(this);
        }
    }

    @Override
    public void visit(AstNode.UpdateMethod updateMethod) {
        sqlType = SqlType.UPDATE;
        sb.append("UPDATE ").append(tableName);
        updateMethod.set().visit(this);
        if (updateMethod.condition() != null) {
            updateMethod.condition().visit(this);
        }
    }

    @Override
    public void visit(AstNode.DeleteMethod deleteMethod) {
        sqlType = SqlType.DELETE;
        sb.append("DELETE FROM ").append(tableName);
        if (deleteMethod.condition() != null) {
            deleteMethod.condition().visit(this);
        }
    }

    @Override
    public void visit(AstNode.Condition condition) {
        sb.append(" WHERE ");
        condition.exp().visit(this);
    }

    @Override
    public void visit(AstNode.EqExp eqExp) {
        eqExp.column().visit(this);
        sb.append(" = ?");
        placeholderIsIn.add(false);
    }

    @Override
    public void visit(AstNode.NeExp neExp) {
        neExp.column().visit(this);
        sb.append(" != ?");
        placeholderIsIn.add(false);
    }

    @Override
    public void visit(AstNode.GtExp gtExp) {
        gtExp.column().visit(this);
        sb.append(" > ?");
        placeholderIsIn.add(false);
    }

    @Override
    public void visit(AstNode.GeExp geExp) {
        geExp.column().visit(this);
        sb.append(" >= ?");
        placeholderIsIn.add(false);
    }

    @Override
    public void visit(AstNode.LeExp leExp) {
        leExp.column().visit(this);
        sb.append(" <= ?");
        placeholderIsIn.add(false);
    }

    @Override
    public void visit(AstNode.LtExp ltExp) {
        ltExp.column().visit(this);
        sb.append(" < ?");
        placeholderIsIn.add(false);
    }

    @Override
    public void visit(AstNode.LikeExp likeExp) {
        likeExp.column().visit(this);
        sb.append(" LIKE ?");
        placeholderIsIn.add(false);
    }

    @Override
    public void visit(AstNode.NotLikeExp notLikeExp) {
        notLikeExp.column().visit(this);
        sb.append(" NOT LIKE ?");
        placeholderIsIn.add(false);
    }

    @Override
    public void visit(AstNode.IsNullExp isNullExp) {
        isNullExp.column().visit(this);
        sb.append(" IS NULL");
    }

    @Override
    public void visit(AstNode.IsNotNullExp isNotNullExp) {
        isNotNullExp.column().visit(this);
        sb.append(" IS NOT NULL");
    }

    @Override
    public void visit(AstNode.BetweenAndExp betweenExp) {
        betweenExp.column().visit(this);
        sb.append(" BETWEEN ? AND ?");
        placeholderIsIn.add(false);
        placeholderIsIn.add(false);
    }

    @Override
    public void visit(AstNode.NotBetweenAndExp notBetweenExp) {
        notBetweenExp.column().visit(this);
        sb.append(" NOT BETWEEN ? AND ?");
        placeholderIsIn.add(false);
        placeholderIsIn.add(false);
    }

    @Override
    public void visit(AstNode.InExp inExp) {
        inExp.column().visit(this);
        sb.append(" IN (?)");
        placeholderIsIn.add(true);
    }

    @Override
    public void visit(AstNode.NotInExp notInExp) {
        notInExp.column().visit(this);
        sb.append(" NOT IN (?)");
        placeholderIsIn.add(true);
    }

    @Override
    public void visit(AstNode.AndExp andExp) {
        andExp.left().visit(this);
        sb.append(" AND ");
        andExp.right().visit(this);
    }

    @Override
    public void visit(AstNode.OrExp orExp) {
        orExp.left().visit(this);
        sb.append(" OR ");
        orExp.right().visit(this);
    }

    @Override
    public void visit(AstNode.Column column) {
        column.prefix().visit(this);
        if (column.suffix() != null) {
            sb.append('_');
            column.suffix().visit(this);
        }
    }

    @Override
    public void visit(AstNode.ColumnNameSegment columnNameSegment) {
        sb.append(columnNameSegment.segment());
    }

    @Override
    public void visit(AstNode.SetPart setPart) {
        sb.append(" SET ");
        setPart.list().visit(this);
    }

    @Override
    public void visit(AstNode.OrderByPart orderByPart) {
        sb.append(" ORDER BY ");
        orderByPart.list().visit(this);
    }

    @Override
    public void visit(AstNode.LimitPart limitPart) {
        sb.append(" LIMIT ?");
        placeholderIsIn.add(false);
    }

    @Override
    public void visit(AstNode.OrderByList orderByList) {
        orderByList.first().visit(this);
        if (orderByList.rest() != null) {
            sb.append(", ");
            orderByList.rest().visit(this);
        }
    }

    @Override
    public void visit(AstNode.OrderByColumn orderByColumn) {
        orderByColumn.column().visit(this);
        sb.append(orderByColumn.asc() ? " ASC" : " DESC");
    }

    @Override
    public void visit(AstNode.SetList setList) {
        setList.first().visit(this);
        sb.append(" = ?");
        placeholderIsIn.add(false);
        if (setList.rest() != null) {
            sb.append(", ");
            setList.rest().visit(this);
        }
    }
}
