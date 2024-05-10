package com.xyzwps.lib.dollar.util;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.dollar.util.MapFactory.*;

public class MapFactoryTests {

    @Test
    void testHashMap() {
        {
            Map<Integer, Integer> map = hashMap();
            assertEquals(0, map.size());
        }
        {
            Map<Integer, Integer> map = hashMap(1, 1);
            assertEquals(1, map.size());

            assertEquals(1, map.get(1));
        }
        {
            Map<Integer, Integer> map = hashMap(1, 1, 2, 2);
            assertEquals(2, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
        }
        {
            Map<Integer, Integer> map = hashMap(1, 1, 2, 2, 3, 3);
            assertEquals(3, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
        }
        {
            Map<Integer, Integer> map = hashMap(1, 1, 2, 2, 3, 3, 4, 4);
            assertEquals(4, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
        }
        {
            Map<Integer, Integer> map = hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5);
            assertEquals(5, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
        }
        {
            Map<Integer, Integer> map = hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6);
            assertEquals(6, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
        }
        {
            Map<Integer, Integer> map = hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7);
            assertEquals(7, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
            assertEquals(7, map.get(7));
        }
        {
            Map<Integer, Integer> map = hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8);
            assertEquals(8, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
            assertEquals(7, map.get(7));
            assertEquals(8, map.get(8));
        }
        {
            Map<Integer, Integer> map = hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9);
            assertEquals(9, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
            assertEquals(7, map.get(7));
            assertEquals(8, map.get(8));
            assertEquals(9, map.get(9));
        }
        {
            Map<Integer, Integer> map = hashMap(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10);
            assertEquals(10, map.size());

            assertEquals(1, map.get(1));
            assertEquals(2, map.get(2));
            assertEquals(3, map.get(3));
            assertEquals(4, map.get(4));
            assertEquals(5, map.get(5));
            assertEquals(6, map.get(6));
            assertEquals(7, map.get(7));
            assertEquals(8, map.get(8));
            assertEquals(9, map.get(9));
            assertEquals(10, map.get(10));
        }
    }

}
