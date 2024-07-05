package com.xyzwps.lib.jdbc.method2sql;

public interface AstNode {

    void visit(AstNodeVisitor visitor);

    sealed interface SqlMethod extends AstNode {
    }

    record FindMethod(Condition condition, OrderByPart orderBy, LimitPart limit) implements SqlMethod {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record CountMethod(Condition condition) implements SqlMethod {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record UpdateMethod(SetPart set, Condition condition) implements SqlMethod {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record DeleteMethod(Condition condition) implements SqlMethod {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }


    record Condition(ConditionExp exp) implements AstNode {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    sealed interface ConditionExp extends AstNode {
    }

    sealed interface BooleanExp extends ConditionExp {
    }

    record EqExp(Column column) implements BooleanExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record NeExp(Column column) implements BooleanExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record GtExp(Column column) implements BooleanExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record GeExp(Column column) implements BooleanExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record LtExp(Column column) implements BooleanExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record LeExp(Column column) implements BooleanExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record LikeExp(Column column) implements BooleanExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record NotLikeExp(Column column) implements BooleanExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record InExp(Column column) implements BooleanExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record NotInExp(Column column) implements BooleanExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record BetweenAndExp(Column column) implements BooleanExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record NotBetweenAndExp(Column column) implements BooleanExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record IsNullExp(Column column) implements BooleanExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record IsNotNullExp(Column column) implements BooleanExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record AndExp(BooleanExp left, ConditionExp right) implements ConditionExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record OrExp(BooleanExp left, ConditionExp right) implements ConditionExp {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record Column(ColumnNameSegment prefix, Column suffix) implements AstNode {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record ColumnNameSegment(String segment) implements AstNode {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record OrderByPart(OrderByList list) implements AstNode {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record OrderByList(OrderByColumn first, OrderByList rest) implements AstNode {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record OrderByColumn(Column column, boolean asc) implements AstNode {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record LimitPart() implements AstNode {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record SetPart(SetList list) implements AstNode {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    record SetList(Column first, SetList rest) implements AstNode {
        @Override
        public void visit(AstNodeVisitor visitor) {
            visitor.visit(this);
        }
    }
}
