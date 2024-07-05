package com.xyzwps.lib.jdbc.method2sql;

import com.xyzwps.lib.jdbc.DbException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class Tokenizer {

    private static final Pattern pattern = Pattern.compile("(([a-z]+)|([A-Z][a-z0-9]*))");

    private static List<String> pieces(String methodName) {
        List<String> elements = new ArrayList<>();
        var matcher = pattern.matcher(methodName);
        while (matcher.find()) {
            elements.add(matcher.group());
        }
        if (!String.join("", elements).equals(methodName)) {
            throw new DbException("Invalid method name " + methodName); // TODO: add table name
        }
        return elements;
    }

    public static List<Token> tokenize(String methodName) {
        var pieces = pieces(methodName);
        var tokens = new ArrayList<Token>();
        for (int i = 0; i < pieces.size(); i++) {
            var p = pieces.get(i);
            if (i == 0) {
                tokens.add(switch (p) {
                    case "find" -> Token.KeyWord.FIND;
                    case "get" -> Token.KeyWord.GET;
                    case "count" -> Token.KeyWord.COUNT;
                    case "update" -> Token.KeyWord.UPDATE;
                    case "delete" -> Token.KeyWord.DELETE;
                    default -> throw new DbException("Unsupported method " + methodName); // TODO: add table name
                });
            } else {
                tokens.add(switch (p) {
                    case "By" -> Token.KeyWord.BY;
                    case "Where" -> Token.KeyWord.WHERE;
                    case "And" -> Token.KeyWord.AND;
                    case "Or" -> Token.KeyWord.OR;
                    case "Eq" -> Token.KeyWord.EQ;
                    case "Ne" -> Token.KeyWord.NE;
                    case "Gt" -> Token.KeyWord.GT;
                    case "Ge" -> Token.KeyWord.GE;
                    case "Lt" -> Token.KeyWord.LT;
                    case "Le" -> Token.KeyWord.LE;
                    case "Like" -> Token.KeyWord.LIKE;
                    case "Not" -> Token.KeyWord.NOT;
                    case "In" -> Token.KeyWord.IN;
                    case "Between" -> Token.KeyWord.BETWEEN;
                    case "Is" -> Token.KeyWord.IS;
                    case "Null" -> Token.KeyWord.NULL;
                    case "Order" -> Token.KeyWord.ORDER;
                    case "Limit" -> Token.KeyWord.LIMIT;
                    case "Asc" -> Token.KeyWord.ASC;
                    case "Desc" -> Token.KeyWord.DESC;
                    case "Set" -> Token.KeyWord.SET;
                    default -> new Token.Name(p.toLowerCase());
                });
            }
        }
        return tokens;
    }

}
