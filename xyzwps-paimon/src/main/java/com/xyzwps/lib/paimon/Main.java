package com.xyzwps.lib.paimon;

public class Main {

    /**
     * 对齐
     * -
     */
    static final String source = """
            package demo;
                        
            import java.util.List;
                        
            public class Main {
                
                public void doSomething() {
                    int i = 1 * 3;
                    char c = 'c';
                    System.out.println("Haha");
                }
            }
             """;

    public static void main(String[] args) {
        System.out.println("-> chars");
        var reader = new StringCharReader(source);
        while (true) {
            var next = reader.nextCh();
            System.out.println(next);
            if (next.ch() == '\0') {
                break;
            }
        }

        System.out.println("-> lexer");
        var lexer = new Lexer(source);
        while (true) {
            var next = lexer.getNextToken();
            System.out.println(next);
            if (next.type() == TokenType.EOF) {
                break;
            }
        }
    }
}
