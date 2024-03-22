package com.xyzwps.lib.paimon;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.xyzwps.lib.paimon.CharReader.Char;

class CharReaderTests {

    static List<Char> toChars(String fileName) {
        var source = Utils.readFileContent(fileName);
        var reader = new CharReader(source);
        var result = new ArrayList<Char>();
        while (true) {
            var next = reader.nextCh();
            if (next.ch() == '\0') {
                break;
            }
        }
        return result;
    }

    @Test
    void helloWorld() {
        toChars("HelloWorld.paimon").forEach(System.out::println);
        // TODO: 怎么编写测试
    }

    @Test
    void fibonacci() {
        toChars("Fibonacci.paimon").forEach(System.out::println);
    }
}
