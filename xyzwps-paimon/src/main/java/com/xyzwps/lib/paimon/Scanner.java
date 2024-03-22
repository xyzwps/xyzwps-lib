package com.xyzwps.lib.paimon;

import java.util.Map;

import static com.xyzwps.lib.paimon.TokenType.*;

public class Scanner {

    private char ch;
    private int line;
    private StringBuffer buffer;

    private final CharReader reader;

    public Scanner(String source) {
        this.reader = new CharReader(source);
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

            // TODO: 这里是把注释当成了空白处理
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
            case '+':
                nextCh();
                if (ch == '=') {
                    nextCh();
                    return new TokenInfo(PLUS_ASSIGN, "+=", line);
                } else {
                    return new TokenInfo(PLUS, "+", line);
                }
            case '-':
                nextCh();
                if (ch == '=') {
                    nextCh();
                    return new TokenInfo(MINUS_ASSIGN, "-=", line);
                } else if (ch == '>') {
                    nextCh();
                    return new TokenInfo(ARROW, "->", line);
                } else {
                    return new TokenInfo(MINUS, "-", line);
                }
            case '*':
                nextCh();
                if (ch == '=') {
                    nextCh();
                    return new TokenInfo(STAT_ASSIGN, "*=", line);
                } else {
                    return new TokenInfo(STAR, "*", line);
                }
            case '/':
                // TODO: 处理注释
                nextCh();
                if (ch == '=') {
                    nextCh();
                    return new TokenInfo(DIV_ASSIGN, "/=", line);
                } else {
                    return new TokenInfo(DIV, "/", line);
                }
            case '<':
                nextCh();
                if (ch == '=') {
                    nextCh();
                    return new TokenInfo(LE, "<=", line);
                } else {
                    return new TokenInfo(LT, "<", line);
                }
            case '>':
                nextCh();
                if (ch == '=') {
                    nextCh();
                    return new TokenInfo(GE, ">=", line);
                } else {
                    return new TokenInfo(GT, ">", line);
                }
            case ';':
                nextCh();
                return new TokenInfo(SEMI, ";", line);
            case ',':
                nextCh();
                return new TokenInfo(COMMA, ",", line);
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
            case '\'':
                return getCharLiteral();
            case '"':
                return getStringLiteral();
            default: {
                // identifier
                if (isLetter(ch) || ch == '_' || ch == '$') {
                    buffer = new StringBuffer();
                    while (isLetter(ch) || isDigit(ch) || ch == '_' || ch == '$') {
                        buffer.append(ch);
                        nextCh();
                    }

                    var identifier = buffer.toString();
                    return new TokenInfo(reserved.getOrDefault(identifier, IDENTIFIER), identifier, line);
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

    private TokenInfo getStringLiteral() {
        buffer = new StringBuffer();
        nextCh();
        while (ch != '"') {
            /* 字符串不支持换行 */
            if (ch == '\n') {
                reportScannerError("String literal is not enclosed.");
            }
            /* 转义字符 */
            else if (ch == '\\') {
                nextCh();
                if (stringEscapes.containsKey(ch)) {
                    buffer.append(stringEscapes.get(ch));
                    nextCh();
                } else {
                    reportScannerError("String literal: unsupported escaping characters");
                }
            }
            /* 默认情况 */
            else {
                buffer.append(ch);
                nextCh();
            }
        } // end while
        nextCh();
        return new TokenInfo(STR_LITERAL, buffer.toString(), line);
    }

    private TokenInfo getCharLiteral() {
        nextCh();
        switch (ch) {
            case '\'' /* 单引号后面不能紧跟单引号 */ -> {
                reportScannerError("Invalid char");
            }
            case '\\' /* 转义字符 */ -> {
                nextCh();
                if (charEscapes.containsKey(ch)) {
                    buffer = new StringBuffer();
                    buffer.append(charEscapes.get(ch));
                    nextCh();
                    if (ch == '\'') {
                        nextCh();
                        return new TokenInfo(CHAR_LITERAL, buffer.toString(), line);
                    } else {
                        reportScannerError("Invalid char");
                    }
                } else {
                    reportScannerError("Invalid char");
                }
            }
            default -> {
                buffer = new StringBuffer();
                buffer.append(ch);
                nextCh();
                if (ch == '\'') {
                    nextCh();
                    return new TokenInfo(CHAR_LITERAL, buffer.toString(), line);
                } else {
                    reportScannerError("Invalid char");
                }
            }
        }

        throw new UnreachableBranchException();
    }

    private static final Map<String, TokenType> reserved = Map.ofEntries(
            Map.entry("abstract", ABSTRACT),
            Map.entry("as", AS),
            Map.entry("async", ASYNC),
            Map.entry("break", BREAK),
            Map.entry("await", AWAIT),
            Map.entry("boolean", BOOLEAN),
            Map.entry("case", CASE),
            Map.entry("char", CHAR_LITERAL),
            Map.entry("const", CONST),
            Map.entry("continue", CONTINUE),
            Map.entry("class", CLASS),
            Map.entry("default", DEFAULT),
            Map.entry("do", DO),
            Map.entry("double", DOUBLE),
            Map.entry("else", ELSE),
            Map.entry("enum", ENUM),
            Map.entry("externs", EXTENDS),
            Map.entry("final", FINAL),
            Map.entry("float", FLOAT),
            Map.entry("for", FOR),
            Map.entry("goto", GOTO),
            Map.entry("if", IF),
            Map.entry("implements", IMPLEMENTS),
            Map.entry("import", IMPORT),
            Map.entry("int", INT),
            Map.entry("interface", INTERFACE),
            Map.entry("long", LONG),
            Map.entry("new", NEW),
            Map.entry("null", NULL),
            Map.entry("open", OPEN),
            Map.entry("private", PRIVATE),
            Map.entry("protected", PROTECTED),
            Map.entry("public", PUBLIC),
            Map.entry("record", RECORD),
            Map.entry("return", RETURN),
            Map.entry("sealed", SEALED),
            Map.entry("static", STATIC),
            Map.entry("string", STRING),
            Map.entry("short", SHORT),
            Map.entry("switch", SWITCH),
            Map.entry("throw", THROW),
            Map.entry("throws", THROWS),
            Map.entry("unsigned", UNSIGNED),
            Map.entry("val", VAL),
            Map.entry("var", VAR),
            Map.entry("void", VOID),
            Map.entry("while", WHILE)
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

    private static final Map<Character, Character> charEscapes = Map.of(
            'f', '\f',
            'n', '\n',
            'r', '\r',
            't', '\t',
            '\'', '\'',
            '\\', '\\'
    );

    private static final Map<Character, Character> stringEscapes = Map.of(
            'f', '\f',
            'n', '\n',
            'r', '\r',
            't', '\t',
            '"', '"',
            '\\', '\\'
    );
}
