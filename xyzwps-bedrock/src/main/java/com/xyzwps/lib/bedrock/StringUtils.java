package com.xyzwps.lib.bedrock;

import java.util.function.IntFunction;
import java.util.regex.Pattern;

public final class StringUtils {

    /**
     * Replace all substrings that match the given pattern with the result of the function.
     * For example:
     * <pre>
     * var pattern = Pattern.compile("\\{}");
     * System.out.println(replaceAll("a={} b={} c={}", pattern, i -> "{" + i + "}"));
     * </pre>
     * Output:
     * <pre>
     * a={0} b={1} c={2}
     * </pre>
     * <p>
     * If any of the arguments is {@code null}, the method will return the original string.
     *
     * @param string         to be replaced
     * @param pattern        to match
     * @param replacementGen to generate the replacement string by index
     * @return the replaced string
     */
    public static String replaceAll(String string, Pattern pattern, IntFunction<String> replacementGen) {
        if (string == null || pattern == null || replacementGen == null) {
            return string;
        }

        var matcher = pattern.matcher(string);
        boolean result = matcher.find();
        if (result) {
            StringBuilder sb = new StringBuilder();
            int start = 0;
            do {
                matcher.appendReplacement(sb, replacementGen.apply(start++));
                result = matcher.find();
            } while (result);
            matcher.appendTail(sb);
            return sb.toString();
        }
        return string;
    }

    private StringUtils() throws IllegalAccessException {
        throw new IllegalAccessException("??");
    }
}
