package com.xyzwps.lib.paimon;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ScannerTests {

    static List<TokenInfo> toTokens(String fileName) {
        var source = Utils.readFileContent(fileName);
        var scanner = new Scanner(source);
        var result = new ArrayList<TokenInfo>();
        while (true) {
            var token = scanner.getNextToken();
            result.add(token);
            if (token.type() == TokenType.EOF) {
                break;
            }
        }
        return result;
    }

    @Test
    void helloWorld() {
        toTokens("HelloWorld.paimon").forEach(System.out::println);
        // TODO: 怎么编写测试
    }

    @Test
    void fibonacci() {
        toTokens("Fibonacci.paimon").forEach(System.out::println);
    }

    public static void main(String[] args) {
        toTokens("Fibonacci.paimon").forEach(System.out::println);
    }
}
