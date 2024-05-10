package com.xyzwps.lib.express;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.express.HttpSearchParams.parse;

class HttpSearchParamsTests {

    @Test
    void test() {
        assertEquals("", parse(null).toHString());
        assertEquals("", parse("").toHString());
        assertEquals("a=", parse("a").toHString());
        assertEquals("a=&a=", parse("a&a").toHString());

        assertEquals("=1", parse("=1").toHString());
        assertEquals("=", parse("=").toHString());
        assertEquals(" =1", parse(" =1").toHString());

        assertEquals("aaa=刻晴", parse("aaa=%E5%88%BB%E6%99%B4").toHString());
        assertEquals("刻晴=aaa", parse("%E5%88%BB%E6%99%B4=aaa").toHString());

        assertEquals("刻晴=====aaa", parse("%E5%88%BB%E6%99%B4=====aaa").toHString());
        assertEquals("====aaa", parse("%E5%88%BB%E6%99%B4=====aaa").get("刻晴"));

        assertEquals("aaa=刻晴&aaa=123", parse("aaa=%E5%88%BB%E6%99%B4&aaa=123").toHString());
        assertIterableEquals(List.of("刻晴", "123"), parse("aaa=%E5%88%BB%E6%99%B4&aaa=123").getAll("aaa"));
        assertIterableEquals(List.of(), parse("aaa=%E5%88%BB%E6%99%B4&aaa=123").getAll("bbb"));
        assertEquals("aaa=刻晴&bbb=123", parse("aaa=%E5%88%BB%E6%99%B4&bbb=123").toHString());
    }

}
