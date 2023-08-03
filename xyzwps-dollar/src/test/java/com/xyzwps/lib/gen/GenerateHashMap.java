package com.xyzwps.lib.gen;

import java.util.ArrayList;
import java.util.List;

public class GenerateHashMap {

    private static void println(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 11; i++) {
            printFn(i);
            println("\n");
        }
    }

    private static void printFn(int argsCount) {
        println("/**");
        println(" * Create a {@link HashMap} with key-value pairs.");
        println(" *");
        for (int i = 1; i <= argsCount; i++) {
            println(" * @param k" + i + "  the " + Nth.get(i) + " key");
            println(" * @param v" + i + "  the " + Nth.get(i) + " value");
        }
        println(" * @param <K> key type");
        println(" * @param <V> value type");
        println(" * @return new HashMap");
        println(" */");
        println("public static <K, V> Map<K, V> mapOf(" + args(argsCount) + ") {");
        if (argsCount == 0) {
            println("    return new HashMap<>();");
        } else {
            println("    HashMap<K, V> map = new HashMap<>();");
            for (int i = 1; i <= argsCount; i++) {
                println("    map.put(k" + i + ", v" + i + ");");
            }
            println("    return map;");
        }
        println("}");
    }

    private static String args(int n) {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add("K k" + i + ", V v" + i);
        }
        return String.join(", ", list);
    }

}

