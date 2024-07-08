package com.xyzwps.lib.express.server.bio.util;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class CRLFLineCallbackReaderTests {

    @Test
    void test() {
        var in = new ByteArrayInputStream((
                "HTTP1.1 /ab/c GET\r\n" +
                "Auth: xx\r\r\rx\r\n" +
                "\r\n" +
                "\r\n"
        ).getBytes());
        var reader = new CRLFLineCallbackReader(in);
        assertEquals("HTTP1.1 /ab/c GET", reader.readLine(StandardCharsets.ISO_8859_1));
        var xx = reader.readLine(StandardCharsets.ISO_8859_1);
        assertEquals("Auth: xx\r\r\rx", xx);
        assertEquals("", reader.readLine(StandardCharsets.ISO_8859_1));
        assertEquals("", reader.readLine(StandardCharsets.ISO_8859_1));
        assertNull(reader.readLine(StandardCharsets.ISO_8859_1));
        assertNull(reader.readLine(StandardCharsets.ISO_8859_1));
    }
}
