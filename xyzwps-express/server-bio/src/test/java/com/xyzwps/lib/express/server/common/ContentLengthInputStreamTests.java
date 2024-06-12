package com.xyzwps.lib.express.server.common;

import com.xyzwps.lib.express.server.bio.common.ContentLengthInputStream;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class ContentLengthInputStreamTests {

    interface InConsumer {
        void accept(InputStream in) throws IOException;
    }


    static void controlGroup12345678(InConsumer consumer) throws IOException {
        try (var byteArrayInputStream = new ByteArrayInputStream("12345678".getBytes())) {
            consumer.accept(byteArrayInputStream);
        }
    }

    static void contentLength12345678(InConsumer consumer) throws IOException {
        try (var byteArrayInputStream = new ByteArrayInputStream("123456789".getBytes())) {
            try (var in = new ContentLengthInputStream(byteArrayInputStream, 8)) {
                consumer.accept(in);
            }
        }
    }

    @Test
    void testRead0() throws IOException {
        InConsumer test1 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            assertEquals('3', in.read());
            assertEquals(5, in.available());

            assertEquals('4', in.read());
            assertEquals(4, in.available());

            assertEquals('5', in.read());
            assertEquals(3, in.available());

            assertEquals('6', in.read());
            assertEquals(2, in.available());

            assertEquals('7', in.read());
            assertEquals(1, in.available());

            assertEquals('8', in.read());
            assertEquals(0, in.available());

            assertEquals(-1, in.read());
        };
        controlGroup12345678(test1);
        contentLength12345678(test1);

        InConsumer test2 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            assertEquals('3', in.read());
            assertEquals(5, in.available());

            assertEquals('4', in.read());
            assertEquals(4, in.available());

            assertEquals('5', in.read());
            assertEquals(3, in.available());

            assertEquals('6', in.read());
            assertEquals(2, in.available());

            assertEquals('7', in.read());
            assertEquals(1, in.available());

            assertEquals('8', in.read());
            assertEquals(0, in.available());

            assertEquals(-1, in.read());
        };
        controlGroup12345678(test2);
        contentLength12345678(test2);

        InConsumer test3 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            assertEquals('3', in.read());
            assertEquals(5, in.available());

            assertEquals('4', in.read());
            assertEquals(4, in.available());

            assertEquals('5', in.read());
            assertEquals(3, in.available());

            assertEquals('6', in.read());
            assertEquals(2, in.available());

            assertEquals('7', in.read());
            assertEquals(1, in.available());

            assertEquals('8', in.read());
            assertEquals(0, in.available());

            assertEquals(-1, in.read());
        };
        controlGroup12345678(test3);
        contentLength12345678(test3);
    }

    @Test
    void testRead1() throws IOException {
        InConsumer test1 = in -> {
            assertEquals(8, in.available());

            byte[] bytes = new byte[5];
            assertEquals(5, in.read(bytes));
            assertArrayEquals("12345".getBytes(), bytes);
            assertEquals(3, in.available());

            assertEquals(3, in.read(bytes));
            assertArrayEquals("67845".getBytes(), bytes);
            assertEquals(0, in.available());

            assertEquals(-1, in.read(bytes));
        };
        controlGroup12345678(test1);
        contentLength12345678(test1);
    }

    @Test
    void testRead3() throws IOException {
        InConsumer test1 = in -> {
            assertEquals(8, in.available());
            var bytes = new byte[5];
            {
                var readCount = in.read(bytes, 0, bytes.length);
                assertEquals(5, readCount);
                assertEquals(3, in.available());
                assertArrayEquals("12345".getBytes(), bytes);
            }
            {
                var readCount = in.read(bytes, 0, bytes.length);
                assertEquals(3, readCount);
                assertEquals(0, in.available());
                assertArrayEquals("67845".getBytes(), bytes);
            }
            {
                var readCount = in.read(new byte[5], 0, 5);
                assertEquals(-1, readCount);
            }
        };
        controlGroup12345678(test1);
        contentLength12345678(test1);

        InConsumer test2 = in -> {
            assertEquals(8, in.available());
            var bytes = new byte[]{'0', '0', '0', '0', '0'};

            {
                var readCount = in.read(bytes, 2, bytes.length - 2);
                assertEquals(3, readCount);
                assertEquals(5, in.available());
                assertArrayEquals("00123".getBytes(), bytes);
            }
            {
                var readCount = in.read(bytes, 2, bytes.length - 2);
                assertEquals(3, readCount);
                assertEquals(2, in.available());
                assertArrayEquals("00456".getBytes(), bytes);
            }
            {
                var readCount = in.read(bytes, 2, bytes.length - 2);
                assertEquals(2, readCount);
                assertEquals(0, in.available());
                assertArrayEquals("00786".getBytes(), bytes);
            }
            {
                var readCount = in.read(bytes, 2, bytes.length - 2);
                assertEquals(-1, readCount);
            }
        };
        controlGroup12345678(test2);
        contentLength12345678(test2);
    }

    @Test
    void testClose() throws IOException {
        try (var byteArrayInputStream = new ByteArrayInputStream("123456789".getBytes())) {
            var in = new ContentLengthInputStream(byteArrayInputStream, 8);
            in.close();
            var ex1 = assertThrows(IOException.class, in::read);
            assertEquals("Stream closed.", ex1.getMessage());
            var ex2 = assertThrows(IOException.class, in::close);
            assertEquals("Stream closed.", ex2.getMessage());
        }
    }

    @Test
    void testReadAllBytes() throws IOException {
        InConsumer test1 = in -> {
            assertEquals(8, in.available());
            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertArrayEquals("12345678".getBytes(), bytes);
            }
            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test1);
        contentLength12345678(test1);

        InConsumer test2 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertArrayEquals("2345678".getBytes(), bytes);
            }
            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test2);
        contentLength12345678(test2);

        InConsumer test3 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertArrayEquals("345678".getBytes(), bytes);
            }
            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test3);
        contentLength12345678(test3);

        InConsumer test4 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            assertEquals('3', in.read());
            assertEquals(5, in.available());

            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertArrayEquals("45678".getBytes(), bytes);
            }
            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test4);
        contentLength12345678(test4);

        InConsumer test5 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            assertEquals('3', in.read());
            assertEquals(5, in.available());

            assertEquals('4', in.read());
            assertEquals(4, in.available());

            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertArrayEquals("5678".getBytes(), bytes);
            }
            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test5);
        contentLength12345678(test5);

        InConsumer test6 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            assertEquals('3', in.read());
            assertEquals(5, in.available());

            assertEquals('4', in.read());
            assertEquals(4, in.available());

            assertEquals('5', in.read());
            assertEquals(3, in.available());

            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertArrayEquals("678".getBytes(), bytes);
            }
            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test6);
        contentLength12345678(test6);

        InConsumer test7 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            assertEquals('3', in.read());
            assertEquals(5, in.available());

            assertEquals('4', in.read());
            assertEquals(4, in.available());

            assertEquals('5', in.read());
            assertEquals(3, in.available());

            assertEquals('6', in.read());
            assertEquals(2, in.available());

            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertArrayEquals("78".getBytes(), bytes);
            }
            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test7);
        contentLength12345678(test7);

        InConsumer test8 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            assertEquals('3', in.read());
            assertEquals(5, in.available());

            assertEquals('4', in.read());
            assertEquals(4, in.available());

            assertEquals('5', in.read());
            assertEquals(3, in.available());

            assertEquals('6', in.read());
            assertEquals(2, in.available());

            assertEquals('7', in.read());
            assertEquals(1, in.available());

            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertArrayEquals("8".getBytes(), bytes);
            }
            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test8);
        contentLength12345678(test8);

        InConsumer test9 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            assertEquals('3', in.read());
            assertEquals(5, in.available());

            assertEquals('4', in.read());
            assertEquals(4, in.available());

            assertEquals('5', in.read());
            assertEquals(3, in.available());

            assertEquals('6', in.read());
            assertEquals(2, in.available());

            assertEquals('7', in.read());
            assertEquals(1, in.available());

            assertEquals('8', in.read());
            assertEquals(0, in.available());

            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertArrayEquals("".getBytes(), bytes);
            }
            {
                var bytes = in.readAllBytes();
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test9);
        contentLength12345678(test9);
    }

    @Test
    void testReadNBytes1() throws IOException {
        InConsumer test1 = in -> {
            assertEquals(8, in.available());
            {
                var bytes = in.readNBytes(5);
                assertEquals(3, in.available());
                assertArrayEquals("12345".getBytes(), bytes);
            }
            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertArrayEquals("678".getBytes(), bytes);
            }
            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test1);
        contentLength12345678(test1);

        InConsumer test2 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            {
                var bytes = in.readNBytes(5);
                assertEquals(2, in.available());
                assertArrayEquals("23456".getBytes(), bytes);
            }
            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertArrayEquals("78".getBytes(), bytes);
            }
            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test2);
        contentLength12345678(test2);

        InConsumer test3 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            {
                var bytes = in.readNBytes(5);
                assertEquals(1, in.available());
                assertArrayEquals("34567".getBytes(), bytes);
            }
            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertArrayEquals("8".getBytes(), bytes);
            }
            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test3);
        contentLength12345678(test3);

        InConsumer test4 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            assertEquals('3', in.read());
            assertEquals(5, in.available());

            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertArrayEquals("45678".getBytes(), bytes);
            }
            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test4);
        contentLength12345678(test4);

        InConsumer test5 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            assertEquals('3', in.read());
            assertEquals(5, in.available());

            assertEquals('4', in.read());
            assertEquals(4, in.available());

            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertArrayEquals("5678".getBytes(), bytes);
            }
            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test5);
        contentLength12345678(test5);

        InConsumer test6 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            assertEquals('3', in.read());
            assertEquals(5, in.available());

            assertEquals('4', in.read());
            assertEquals(4, in.available());

            assertEquals('5', in.read());
            assertEquals(3, in.available());

            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertArrayEquals("678".getBytes(), bytes);
            }
            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test6);
        contentLength12345678(test6);

        InConsumer test7 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            assertEquals('3', in.read());
            assertEquals(5, in.available());

            assertEquals('4', in.read());
            assertEquals(4, in.available());

            assertEquals('5', in.read());
            assertEquals(3, in.available());

            assertEquals('6', in.read());
            assertEquals(2, in.available());

            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertArrayEquals("78".getBytes(), bytes);
            }
            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test7);
        contentLength12345678(test7);

        InConsumer test8 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            assertEquals('3', in.read());
            assertEquals(5, in.available());

            assertEquals('4', in.read());
            assertEquals(4, in.available());

            assertEquals('5', in.read());
            assertEquals(3, in.available());

            assertEquals('6', in.read());
            assertEquals(2, in.available());

            assertEquals('7', in.read());
            assertEquals(1, in.available());

            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertArrayEquals("8".getBytes(), bytes);
            }
            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test8);
        contentLength12345678(test8);

        InConsumer test9 = in -> {
            assertEquals(8, in.available());

            assertEquals('1', in.read());
            assertEquals(7, in.available());

            assertEquals('2', in.read());
            assertEquals(6, in.available());

            assertEquals('3', in.read());
            assertEquals(5, in.available());

            assertEquals('4', in.read());
            assertEquals(4, in.available());

            assertEquals('5', in.read());
            assertEquals(3, in.available());

            assertEquals('6', in.read());
            assertEquals(2, in.available());

            assertEquals('7', in.read());
            assertEquals(1, in.available());

            assertEquals('8', in.read());
            assertEquals(0, in.available());

            {
                var bytes = in.readNBytes(5);
                assertEquals(0, in.available());
                assertEquals(0, bytes.length);
            }
        };
        controlGroup12345678(test9);
        contentLength12345678(test9);
    }

    @Test
    void testReadNBytes3() throws IOException {
        InConsumer test1 = in -> {
            assertEquals(8, in.available());
            var bytes = new byte[]{'-', '-', '-', '-', '-'};
            {
                int count = in.readNBytes(bytes, 0, 5);
                assertEquals(5, count);
                assertEquals(3, in.available());
                assertArrayEquals("12345".getBytes(), bytes);
            }
            {
                int count = in.readNBytes(bytes, 0, 5);
                assertEquals(3, count);
                assertEquals(0, in.available());
                assertArrayEquals("67845".getBytes(), bytes);
            }
            {
                int count = in.readNBytes(bytes, 0, 5);
                assertEquals(0, count);
                assertEquals(0, in.available());
                assertArrayEquals("67845".getBytes(), bytes);
            }
        };
        controlGroup12345678(test1);
        contentLength12345678(test1);

        InConsumer test2 = in -> {
            assertEquals(8, in.available());
            var bytes = new byte[]{'-', '-', '-', '-', '-'};
            {
                int count = in.readNBytes(bytes, 1, 4);
                assertEquals(4, count);
                assertEquals(4, in.available());
                assertArrayEquals("-1234".getBytes(), bytes);
            }
            {
                int count = in.readNBytes(bytes, 1, 4);
                assertEquals(4, count);
                assertEquals(0, in.available());
                assertArrayEquals("-5678".getBytes(), bytes);
            }
            {
                int count = in.readNBytes(bytes, 0, 5);
                assertEquals(0, count);
                assertEquals(0, in.available());
                assertArrayEquals("-5678".getBytes(), bytes);
            }
        };
        controlGroup12345678(test2);
        contentLength12345678(test2);

        InConsumer test3 = in -> {
            assertEquals(8, in.available());
            var bytes = new byte[]{'-', '-', '-', '-', '-'};
            {
                int count = in.readNBytes(bytes, 1, 3);
                assertEquals(3, count);
                assertEquals(5, in.available());
                assertArrayEquals("-123-".getBytes(), bytes);
            }
            {
                int count = in.readNBytes(bytes, 1, 3);
                assertEquals(3, count);
                assertEquals(2, in.available());
                assertArrayEquals("-456-".getBytes(), bytes);
            }
            {
                int count = in.readNBytes(bytes, 1, 3);
                assertEquals(2, count);
                assertEquals(0, in.available());
                assertArrayEquals("-786-".getBytes(), bytes);
            }
            {
                int count = in.readNBytes(bytes, 0, 5);
                assertEquals(0, count);
                assertEquals(0, in.available());
                assertArrayEquals("-786-".getBytes(), bytes);
            }
        };
        controlGroup12345678(test3);
        contentLength12345678(test3);
    }

    @Test
    void testSkip1() throws IOException {
        InConsumer test1 = in -> {
            assertEquals(8, in.available());

            assertEquals(0, in.skip(0));
            assertEquals(8, in.available());

            assertEquals(0, in.skip(-3));
            assertEquals(8, in.available());

            assertEquals(3, in.skip(3));
            assertEquals(5, in.available());

            assertEquals(3, in.skip(3));
            assertEquals(2, in.available());

            assertEquals(2, in.skip(3));
            assertEquals(0, in.available());
        };
        controlGroup12345678(test1);
        contentLength12345678(test1);
    }
}
