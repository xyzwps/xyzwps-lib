class GenerateCases {

    static void main(String[] args) {

        List<String> TYPES = List.of("primitive", "object", "generic", "boolean");
        List<String> AC = List.of("private", "protected", "public", "package");
        List<Integer> HAS_FIELD = List.of(0, 1);
        List<Integer> HAS_GETTER = List.of(0, 1);
        List<Integer> HAS_SETTER = List.of(0, 1);

        for (typeIndex in 0..<TYPES.size()) {
            for (acIndex in 0..<AC.size()) {
                for (def hasField : HAS_FIELD) {
                    for (def hasGetter : HAS_GETTER) {
                        for (def hasSetter : HAS_SETTER) {
                            printClass(TYPES[typeIndex], typeIndex + 1, AC[acIndex], acIndex + 1, hasField, hasGetter, hasSetter);
                        }
                    }
                }
            }
        }
    }

    static void printClass(String type, int typeIndex, String ac, int acIndex, int hasField, int hasGetter, int hasSetter) {
def code = """
package com.xyzwps.lib.beans;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 无继承 positive case;
 * <pre>
 *   C
 *   $typeIndex - type $type
 *   $acIndex - ac $ac
 *   $hasField - ${hasField ? "has" : "no"} field
 *   $hasGetter - ${hasGetter ? "has" : "no"} getter
 *   $hasSetter - ${hasSetter ? "has" : "no"} setter
 * </pre>
 */
class TestC${typeIndex}${acIndex}${hasField}${hasGetter}${hasSetter} {

    @Test
    void test() {
      
    }              
}
"""
        var file = new File("TestC${typeIndex}${acIndex}${hasField}${hasGetter}${hasSetter}.java")
        file << code
    }
}
