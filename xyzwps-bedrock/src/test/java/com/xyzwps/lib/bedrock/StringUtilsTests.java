package com.xyzwps.lib.bedrock;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTests {

    @Test
    void replaceAll() {
        var pattern = Pattern.compile("\\{}");

        assertNull(StringUtils.replaceAll(null, null, null));
        assertEquals("abc", StringUtils.replaceAll("abc", null, null));
        assertEquals("abc", StringUtils.replaceAll("abc", pattern, null));

        assertEquals("a={0} b={1} c={2}", StringUtils.replaceAll("a={} b={} c={}", pattern, i -> "{" + i + "}"));

    }
}
