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
                    char c, d, e, f = 'c', '\\n', '\\\\', '\\'';
                    System.out.println("Haha\\t\\\\\\"");
                }
            }
             """;

    public static void main(String[] args) {
        System.out.println("-> chars");
        var reader = new CharReader(source);
        while (true) {
            var next = reader.nextCh();
            System.out.println(next);
            if (next.ch() == '\0') {
                break;
            }
        }

        System.out.println("-> scanner");
        var scanner = new Scanner(source);
        while (true) {
            var next = scanner.getNextToken();
            System.out.println(next);
            if (next.type() == TokenType.EOF) {
                break;
            }
        }
    }
}
