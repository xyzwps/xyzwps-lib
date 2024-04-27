package com.xyzwps.lib.express.server.common;

import com.xyzwps.lib.express.server.common.ContentLengthInputStream;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ContentLengthInputStreamTests {

    @Test
    void testRead0() throws IOException {

        // control group
        try (var byteArrayInputStream = new ByteArrayInputStream("123456789".getBytes())) {
            assertEquals('1', byteArrayInputStream.read());
            assertEquals('2', byteArrayInputStream.read());
            assertEquals('3', byteArrayInputStream.read());
            assertEquals('4', byteArrayInputStream.read());
            assertEquals('5', byteArrayInputStream.read());
            assertEquals('6', byteArrayInputStream.read());
            assertEquals('7', byteArrayInputStream.read());
            assertEquals('8', byteArrayInputStream.read());
            assertEquals('9', byteArrayInputStream.read());
            assertEquals(-1, byteArrayInputStream.read());
        }

        try (var byteArrayInputStream = new ByteArrayInputStream("123456789".getBytes())) {
            try (var clis = new ContentLengthInputStream(byteArrayInputStream, 4, 8)) {
                assertEquals('1', clis.read());
                assertEquals('2', clis.read());
                assertEquals('3', clis.read());
                assertEquals('4', clis.read());
                assertEquals('5', clis.read());
                assertEquals('6', clis.read());
                assertEquals('7', clis.read());
                assertEquals('8', clis.read());
                assertEquals(-1, clis.read());
            }
        }

        try (var byteArrayInputStream = new ByteArrayInputStream("123456789".getBytes())) {
            try (var clis = new ContentLengthInputStream(byteArrayInputStream, 5, 8)) {
                assertEquals('1', clis.read());
                assertEquals('2', clis.read());
                assertEquals('3', clis.read());
                assertEquals('4', clis.read());
                assertEquals('5', clis.read());
                assertEquals('6', clis.read());
                assertEquals('7', clis.read());
                assertEquals('8', clis.read());
                assertEquals(-1, clis.read());
            }
        }

        try (var byteArrayInputStream = new ByteArrayInputStream("123456789".getBytes())) {
            try (var clis = new ContentLengthInputStream(byteArrayInputStream, 100, 8)) {
                assertEquals('1', clis.read());
                assertEquals('2', clis.read());
                assertEquals('3', clis.read());
                assertEquals('4', clis.read());
                assertEquals('5', clis.read());
                assertEquals('6', clis.read());
                assertEquals('7', clis.read());
                assertEquals('8', clis.read());
                assertEquals(-1, clis.read());
            }
        }
    }

    @Test
    void testRead3() throws IOException {
        // control group 1
        {
            try (var byteArrayInputStream = new ByteArrayInputStream("123456789".getBytes())) {
                {
                    var bytes = new byte[5];
                    var readCount = byteArrayInputStream.read(bytes, 0, bytes.length);
                    assertEquals(5, readCount);
                    assertEquals(4, byteArrayInputStream.available());
                    assertArrayEquals("12345".getBytes(), bytes);
                }
                {
                    var bytes = new byte[5];
                    var readCount = byteArrayInputStream.read(bytes, 0, bytes.length);
                    assertEquals(4, readCount);
                    bytes[4] = "5".getBytes()[0];
                    assertEquals(0, byteArrayInputStream.available());
                    assertArrayEquals("67895".getBytes(), bytes);
                }
                {
                    var readCount = byteArrayInputStream.read(new byte[5], 0, 5);
                    assertEquals(-1, readCount);
                }
            }

            for (int i = 1; i < 20; i++) {
                try (var byteArrayInputStream = new ByteArrayInputStream("123456789".getBytes())) {
                    try (var clis = new ContentLengthInputStream(byteArrayInputStream, i, 8)) {
                        {
                            var bytes = new byte[5];
                            var readCount = clis.read(bytes, 0, bytes.length);
                            assertEquals(5, readCount);
                            assertEquals(3, clis.available());
                            assertArrayEquals("12345".getBytes(), bytes);
                        }
                        {
                            var bytes = new byte[5];
                            var readCount = clis.read(bytes, 0, bytes.length);
                            assertEquals(3, readCount);
                            bytes[3] = "4".getBytes()[0];
                            bytes[4] = "5".getBytes()[0];
                            assertEquals(0, clis.available());
                            assertArrayEquals("67845".getBytes(), bytes);
                        }
                        {
                            var readCount = clis.read(new byte[5], 0, 5);
                            assertEquals(-1, readCount);
                        }
                    }
                }
            }
        }

        // control group 2
        {
            try (var byteArrayInputStream = new ByteArrayInputStream("123456789".getBytes())) {
                {
                    var bytes = new byte[5];
                    var readCount = byteArrayInputStream.read(bytes, 2, bytes.length - 2);
                    assertEquals(3, readCount);
                    bytes[0] = (byte) '0';
                    bytes[1] = (byte) '0';
                    assertEquals(6, byteArrayInputStream.available());
                    assertArrayEquals("00123".getBytes(), bytes);
                }
                {
                    var bytes = new byte[5];
                    var readCount = byteArrayInputStream.read(bytes, 2, bytes.length - 2);
                    assertEquals(3, readCount);
                    bytes[0] = (byte) '0';
                    bytes[1] = (byte) '0';
                    assertEquals(3, byteArrayInputStream.available());
                    assertArrayEquals("00456".getBytes(), bytes);
                }
                {
                    var bytes = new byte[5];
                    var readCount = byteArrayInputStream.read(bytes, 2, bytes.length - 2);
                    assertEquals(3, readCount);
                    bytes[0] = (byte) '0';
                    bytes[1] = (byte) '0';
                    assertEquals(0, byteArrayInputStream.available());
                    assertArrayEquals("00789".getBytes(), bytes);
                }
                {
                    var bytes = new byte[5];
                    var readCount = byteArrayInputStream.read(bytes, 2, bytes.length - 2);
                    assertEquals(-1, readCount);
                }
            }

            for (int i = 1; i < 20; i++) {
                try (var byteArrayInputStream = new ByteArrayInputStream("123456789".getBytes())) {
                    try (var clis = new ContentLengthInputStream(byteArrayInputStream, i, 8)) {
                        {
                            var bytes = new byte[5];
                            var readCount = clis.read(bytes, 2, bytes.length - 2);
                            assertEquals(3, readCount);
                            bytes[0] = (byte) '0';
                            bytes[1] = (byte) '0';
                            assertEquals(5, clis.available());
                            assertArrayEquals("00123".getBytes(), bytes);
                        }
                        {
                            var bytes = new byte[5];
                            var readCount = clis.read(bytes, 2, bytes.length - 2);
                            assertEquals(3, readCount);
                            bytes[0] = (byte) '0';
                            bytes[1] = (byte) '0';
                            assertEquals(2, clis.available());
                            assertArrayEquals("00456".getBytes(), bytes);
                        }
                        {
                            var bytes = new byte[5];
                            var readCount = clis.read(bytes, 2, bytes.length - 2);
                            assertEquals(2, readCount);
                            bytes[0] = (byte) '0';
                            bytes[1] = (byte) '0';
                            bytes[4] = (byte) '0';
                            assertEquals(0, clis.available());
                            assertArrayEquals("00780".getBytes(), bytes);
                        }
                        {
                            var bytes = new byte[5];
                            var readCount = clis.read(bytes, 2, bytes.length - 2);
                            assertEquals(-1, readCount);
                        }
                    }
                }
            }
        }
    }
}
