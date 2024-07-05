package com.xyzwps.lib.jdbc.method2sql;

import java.util.List;

import static com.xyzwps.lib.jdbc.method2sql.AstNode.*;
import static com.xyzwps.lib.jdbc.method2sql.Token.*;
import static com.xyzwps.lib.jdbc.method2sql.Token.KeyWord.*;

public final class Tokens2Ast {

    private final List<Token> tokens;

    private int curr = 0;

    Token curr() {
        return tokens.get(curr);
    }

    void forward() {
        curr++;
    }

    public boolean eof() {
        return curr >= tokens.size();
    }

    void assertAndForward(KeyWord... keyWords) {
        for (KeyWord keyWord : keyWords) {
            if (keyWord.equals(curr())) {
                forward();
                return;
            }
        }
        throw new IllegalStateException("Unexpected token " + curr());
    }

    public Tokens2Ast(List<Token> tokens) {
        this.tokens = tokens;
    }

    public AstNode parse() {
        return sqlMethod();
    }

    private SqlMethod sqlMethod() {
        return switch (curr()) {
            case KeyWord keyWord -> switch (keyWord) {
                case FIND, GET -> findMethod();
                case COUNT -> countMethod();
                case UPDATE -> updateMethod();
                case DELETE -> deleteMethod();
                default -> throw new IllegalStateException("Unexpected token " + curr());
            };
            default -> throw new IllegalStateException("Unexpected token " + curr());
        };
    }

    private FindMethod findMethod() {
        forward();
        return new FindMethod(condition$(), orderByPart$(), limitPart$());
    }

    private CountMethod countMethod() {
        forward();
        return new CountMethod(condition$());
    }

    private UpdateMethod updateMethod() {
        forward();
        return new UpdateMethod(setPart(), condition$());
    }

    private DeleteMethod deleteMethod() {
        forward();
        return new DeleteMethod(condition$());
    }

    private Condition condition$() {
        if (eof()) {
            return null;
        }

        if (curr().equals(BY) || curr().equals(WHERE)) {
            return condition();
        } else {
            return null;
        }
    }

    private Condition condition() {
        assertAndForward(BY, WHERE);
        var conditionExp = conditionExp();
        return new Condition(conditionExp);
    }

    private ConditionExp conditionExp() {
        var booleanExp = booleanExp();
        if (eof()) {
            return booleanExp;
        }

        if (curr().equals(AND)) {
            forward();
            return new AndExp(booleanExp, conditionExp());
        } else if (curr().equals(OR)) {
            forward();
            return new OrExp(booleanExp, conditionExp());
        } else {
            return booleanExp;
        }
    }

    private BooleanExp booleanExp() {
        var column = column();
        if (eof()) {
            return new EqExp(column);
        }

        return switch (curr()) {
            case EQ -> {
                forward();
                yield new EqExp(column);
            }
            case NE -> {
                forward();
                yield new NeExp(column);
            }
            case GT -> {
                forward();
                yield new GtExp(column);
            }
            case GE -> {
                forward();
                yield new GeExp(column);
            }
            case LT -> {
                forward();
                yield new LtExp(column);
            }
            case LE -> {
                forward();
                yield new LeExp(column);
            }
            case LIKE -> {
                forward();
                yield new LikeExp(column);
            }
            case IN -> {
                forward();
                yield new InExp(column);
            }
            case NULL -> {
                forward();
                yield new IsNullExp(column);
            }
            case BETWEEN -> {
                forward();
                assertAndForward(AND);
                yield new BetweenAndExp(column);
            }
            case NOT -> {
                forward();
                yield switch (curr()) {
                    case LIKE -> {
                        forward();
                        yield new NotLikeExp(column);
                    }
                    case IN -> {
                        forward();
                        yield new NotInExp(column);
                    }
                    case BETWEEN -> {
                        forward();
                        assertAndForward(AND);
                        yield new NotBetweenAndExp(column);
                    }
                    case NULL -> {
                        forward();
                        yield new IsNotNullExp(column);
                    }
                    default -> throw new IllegalStateException("Unexpected token " + curr());
                };
            }
            case IS -> {
                forward();
                yield switch (curr()) {
                    case NULL -> {
                        forward();
                        yield new IsNullExp(column);
                    }
                    case NOT -> {
                        forward();
                        assertAndForward(NULL);
                        yield new IsNotNullExp(column);
                    }
                    default -> throw new IllegalStateException("Unexpected token " + curr());
                };
            }
            default -> new EqExp(column);
        };
    }

    private Column column() {
        if (curr() instanceof Name name) {
            forward();
            var segment = new ColumnNameSegment(name.name());
            return new Column(segment, column$());
        } else {
            throw new IllegalStateException("Unexpected token " + curr());
        }
    }

    private Column column$() {
        if (eof()) {
            return null;
        }

        if (curr() instanceof Name) {
            return column();
        } else {
            return null;
        }
    }

    private OrderByPart orderByPart$() {
        if (eof()) {
            return null;
        }

        if (curr().equals(ORDER)) {
            forward();
            assertAndForward(BY);
            var orderByList = orderByList();
            return new OrderByPart(orderByList);
        } else {
            return null;
        }
    }

    private OrderByList orderByList() {
        var orderByColumn = orderByColumn();
        var orderByList$ = orderByList$();
        return new OrderByList(orderByColumn, orderByList$);
    }

    private OrderByList orderByList$() {
        if (eof()) {
            return null;
        }

        if (curr() instanceof Name) {
            return orderByList();
        } else {
            return null;
        }
    }

    private OrderByColumn orderByColumn() {
        var column = column();
        if (eof()) {
            return new OrderByColumn(column, true);
        }

        return switch (curr()) {
            case ASC -> {
                forward();
                yield new OrderByColumn(column, true);
            }
            case DESC -> {
                forward();
                yield new OrderByColumn(column, false);
            }
            default -> new OrderByColumn(column, true);
        };
    }

    private LimitPart limitPart$() {
        if (eof()) {
            return null;
        }

        if (curr().equals(LIMIT)) {
            forward();
            return new LimitPart();
        } else {
            return null;
        }
    }

    private SetPart setPart() {
        assertAndForward(SET);
        var setList = setList();
        return new SetPart(setList);
    }

    private SetList setList() {
        var column = column();
        if (eof()) {
            return new SetList(column, null);
        }

        if (curr().equals(AND)) {
            forward();
            return new SetList(column, setList());
        } else {
            return new SetList(column, null);
        }
    }

}
