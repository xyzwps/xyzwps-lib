package com.xyzwps.lib.paimon;

import java.util.Map;

import static com.xyzwps.lib.paimon.TokenType.*;

public class Lexer {

    private char ch;
    private int line;
    private StringBuffer buffer;

    private final StringCharReader reader;

    public Lexer(String source) {
        this.reader = new StringCharReader(source);
        this.nextCh();
    }

    private void nextCh() {
        var next = reader.nextCh();
        this.ch = next.ch();
        this.line = next.line();
    }

    public TokenInfo getNextToken() {

        boolean moreWhiteSpace = true;
        while (moreWhiteSpace) {
            while (isWhiteSpace(ch)) {
                nextCh();
            }

            // 这里是把注释当成了空白处理
            if (ch == '/') {
                nextCh();
                if (ch == '/') {
                    while (ch != '\n' && ch != EOFCH) {
                        nextCh();
                    }
                } else {
                    // TODO: 支持 / 操作符
                    reportScannerError("Operator / is not supported in j--.");
                }
            } else {
                moreWhiteSpace = false;
            }
        }

        switch (ch) {
            case EOFCH:
                return new TokenInfo(EOF, "<EOF>", line);
            case ';':
                nextCh();
                return new TokenInfo(SEMI, ";", line);
            case '.':
                nextCh();
                return new TokenInfo(DOT, ".", line);
            case '(':
                nextCh();
                return new TokenInfo(LCURLY, "(", line);
            case ')':
                nextCh();
                return new TokenInfo(RCURLY, ")", line);
            case '[':
                nextCh();
                return new TokenInfo(LBRACK, "[", line);
            case ']':
                nextCh();
                return new TokenInfo(RBRACK, "]", line);
            case '{':
                nextCh();
                return new TokenInfo(LBRACE, "{", line);
            case '}':
                nextCh();
                return new TokenInfo(RBRACE, "}", line);
            case '=':
                nextCh();
                if (ch == '=') {
                    nextCh();
                    return new TokenInfo(EQUAL, "==", line);
                } else {
                    return new TokenInfo(ASSIGN, "=", line);
                }
            case '!':
                nextCh();
                return new TokenInfo(LNOT, "!", line);
            case '*':
                nextCh();
                return new TokenInfo(STAR, "*", line);
            case '\'': {
                nextCh();
                // TODO: 特殊字符
                if (ch == '\'') {
                    reportScannerError("Invalid char");
                }

                buffer = new StringBuffer();
                buffer.append(ch);

                nextCh();
                if (ch == '\'') {
                    nextCh();
                    return new TokenInfo(CHAR, buffer.toString(), line);
                } else {
                    reportScannerError("Invalid char");
                }
            }
            case '"': {
                buffer = new StringBuffer();
                nextCh();
                while (ch != '"') {
                    // TODO: 处理转义字符 | 换行
                    buffer.append(ch);
                    nextCh();
                }
                nextCh();
                return new TokenInfo(STR_LITERAL, buffer.toString(), line);
            }
            default: {
                // identifier
                if (isLetter(ch) || ch == '_' || ch == '$') {
                    buffer = new StringBuffer();
                    while (isLetter(ch) || isDigit(ch) || ch == '_' || ch == '$') {
                        buffer.append(ch);
                        nextCh();
                    }

                    var identifier = buffer.toString();
                    if (reserved.containsKey(identifier)) {
                        return new TokenInfo(reserved.get(identifier), identifier, line);
                    } else {
                        return new TokenInfo(IDENTIFIER, identifier, line);
                    }
                }
                // int literal
                else if (ch == '0') {
                    nextCh();
                    return new TokenInfo(INT_LITERAL, "0", line);
                } else if (isDigit(ch)) {
                    buffer = new StringBuffer();
                    while (isDigit(ch)) {
                        buffer.append(ch);
                        nextCh();
                    }
                    return new TokenInfo(INT_LITERAL, buffer.toString(), line);
                } else {
                    throw new UnsupportedOperationException("TODO: 尚未实现");
                }
            }
        }
    }

    private static final Map<String, TokenType> reserved = Map.ofEntries(
            Map.entry("abstract", ABSTRACT),
            Map.entry("boolean", BOOLEAN),
            // TODO: case
            Map.entry("char", CHAR),
            // TODO: class
            // TODO: default
            Map.entry("double", DOUBLE),
            Map.entry("for", FOR),
            // TODO: import
            Map.entry("int", INT),
            Map.entry("long", LONG),
            Map.entry("new", NEW),
            Map.entry("null", NULL),
            // TODO: private | protected | public
            // TODO: switch
            Map.entry("while", WHILE)
            // TODO: add more reserved words
    );


    private static boolean isLetter(char ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z';
    }

    private static boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private static boolean isWhiteSpace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\f' || ch == '\b' || ch == '\r' || ch == '\n';
    }

    private static final char EOFCH = '\0';

    private static void reportScannerError(String message) {
        throw new UnsupportedOperationException("TODO: 尚未实现 " + message);
    }
}
