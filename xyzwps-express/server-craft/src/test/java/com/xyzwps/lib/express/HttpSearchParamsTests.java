package com.xyzwps.lib.express;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.express.HttpSearchParams.parse;

class HttpSearchParamsTests {

    @Test
    void test() {
        assertEquals("", parse(null).toHumanReadableString());
        assertEquals("", parse("").toHumanReadableString());
        assertEquals("a=", parse("a").toHumanReadableString());
        assertEquals("a=&a=", parse("a&a").toHumanReadableString());

        assertEquals("=1", parse("=1").toHumanReadableString());
        assertEquals("=", parse("=").toHumanReadableString());
        assertEquals(" =1", parse(" =1").toHumanReadableString());

        assertEquals("aaa=刻晴", parse("aaa=%E5%88%BB%E6%99%B4").toHumanReadableString());
        assertEquals("刻晴=aaa", parse("%E5%88%BB%E6%99%B4=aaa").toHumanReadableString());

        assertEquals("刻晴=====aaa", parse("%E5%88%BB%E6%99%B4=====aaa").toHumanReadableString());
        assertEquals("====aaa", parse("%E5%88%BB%E6%99%B4=====aaa").get("刻晴"));

        assertEquals("aaa=刻晴&aaa=123", parse("aaa=%E5%88%BB%E6%99%B4&aaa=123").toHumanReadableString());
        assertIterableEquals(List.of("刻晴", "123"), parse("aaa=%E5%88%BB%E6%99%B4&aaa=123").getAll("aaa"));
        assertIterableEquals(List.of(), parse("aaa=%E5%88%BB%E6%99%B4&aaa=123").getAll("bbb"));
        assertEquals("aaa=刻晴&bbb=123", parse("aaa=%E5%88%BB%E6%99%B4&bbb=123").toHumanReadableString());
    }

}
