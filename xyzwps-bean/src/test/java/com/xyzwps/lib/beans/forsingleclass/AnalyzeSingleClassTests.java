package com.xyzwps.lib.beans.forsingleclass;

import com.xyzwps.lib.beans.BeanUtils;
import com.xyzwps.lib.beans.GetResult;
import com.xyzwps.lib.beans.forold.Holder;
import com.xyzwps.lib.beans.PropertyInfo;
import com.xyzwps.lib.bedrock.lang.Types;
import org.junit.jupiter.api.Test;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

class AnalyzeSingleClassTests {

    @Test
    void analyzeRecord() {
        var bi = BeanUtils.getBeanInfoFromClass(SingleClassExample.class);
        var props = bi.getBeanProperties();
        assertEquals(14, props.size());

        for (var prop : props) {
            assertTrue(prop.readable());
            assertTrue(prop.writable());
        }

        Function<String, PropertyInfo> getProp = (name) -> bi.getPropertyInfo(name).orElse(null);

        _1:
        {
            var prop = getProp.apply("byteValue");
            assertEquals("byteValue", prop.name());
            assertEquals(byte.class, prop.type());
        }

        _2:
        {
            var prop = getProp.apply("shortValue");
            assertEquals("shortValue", prop.name());
            assertEquals(short.class, prop.type());
        }

        _3:
        {
            var prop = getProp.apply("intValue");
            assertEquals("intValue", prop.name());
            assertEquals(int.class, prop.type());
        }

        _4:
        {
            var prop = getProp.apply("longValue");
            assertEquals("longValue", prop.name());
            assertEquals(long.class, prop.type());
        }

        _5:
        {
            var prop = getProp.apply("charValue");
            assertEquals("charValue", prop.name());
            assertEquals(char.class, prop.type());
        }

        _6:
        {
            var prop = getProp.apply("floatValue");
            assertEquals("floatValue", prop.name());
            assertEquals(float.class, prop.type());
        }

        _7:
        {
            var prop = getProp.apply("doubleValue");
            assertEquals("doubleValue", prop.name());
            assertEquals(double.class, prop.type());
        }

        _8:
        {
            var prop = getProp.apply("booleanValue");
            assertEquals("booleanValue", prop.name());
            assertEquals(boolean.class, prop.type());
        }

        _9:
        {
            var prop = getProp.apply("str");
            assertEquals("str", prop.name());
            assertEquals(String.class, prop.type());
        }

        _10:
        {
            var prop = getProp.apply("strList");
            assertEquals("strList", prop.name());
            assertTrue(Types.isParameterizedType(prop.type()));

            var propType = (ParameterizedType) prop.type();
            assertEquals(List.class, propType.getRawType());
            assertEquals(String.class, propType.getActualTypeArguments()[0]);
        }

        _11:
        {
            var prop = getProp.apply("objList");
            assertEquals("objList", prop.name());
            assertTrue(Types.isParameterizedType(prop.type()));

            var propType = (ParameterizedType) prop.type();
            assertEquals(List.class, propType.getRawType());

            var typeArgs0 = propType.getActualTypeArguments()[0];
            assertInstanceOf(WildcardType.class, typeArgs0);
            assertEquals("?", typeArgs0.getTypeName());
        }

        _12:
        {
            var prop = getProp.apply("intArr");
            assertEquals("intArr", prop.name());
            assertTrue(Types.isClass(prop.type())); // array type is class

            var propType = (Class<?>) prop.type();
            assertEquals(int.class, propType.componentType());
        }

        _13:
        {
            var prop = getProp.apply("strArr");
            assertEquals("strArr", prop.name());
            assertTrue(Types.isClass(prop.type())); // array type is class

            var propType = (Class<?>) prop.type();
            assertEquals(String.class, propType.componentType());
        }

        _14:
        {
            var prop = getProp.apply("holderArr");
            assertEquals("holderArr", prop.name());
            assertTrue(Types.isGenericArrayType(prop.type())); // array type is class

            var propType = (GenericArrayType) prop.type();
            var compType = propType.getGenericComponentType();
            assertTrue(Types.isParameterizedType(compType));
        }
    }

    @Test
    void getRecordProperty() {
        var example = new SingleClassExample();
        example.setByteValue((byte) 12);              /*  1 */
        example.setShortValue((short) 233);           /*  2 */
        example.setIntValue(114514);                  /*  3 */
        example.setLongValue(12345678987654321L);     /*  4 */
        example.setCharValue('H');                    /*  5 */
        example.setFloatValue(3.14F);                 /*  6 */
        example.setDoubleValue(6.02e23);              /*  7 */
        example.setBooleanValue(true);                /*  8 */
        example.setStr("Hello world");                /*  9 */
        example.setStrList(List.of("123", "233"));    /* 10 */
        example.setObjList(List.of(1, "23"));         /* 11 */
        example.setIntArr(new int[]{1, 2, 3});        /* 12 */
        example.setStrArr(new String[]{"123", "233"});/* 13 */
        example.setHolderArr(new Holder[]{new Holder<>("123"), new Holder<>("233")});/* 14 */

        var props = BeanUtils.getProperties(example);

        _1:
        {
            assertEquals((byte) 12, props.get("byteValue"));
            var result = BeanUtils.getProperty(example, "byteValue");
            assertInstanceOf(GetResult.Ok.class, result);
            var ok = (GetResult.Ok) result;
            assertEquals((byte) 12, ok.value());
        }

        _2:
        {
            assertEquals((short) 233, props.get("shortValue"));
            var result = BeanUtils.getProperty(example, "shortValue");
            assertInstanceOf(GetResult.Ok.class, result);
            var ok = (GetResult.Ok) result;
            assertEquals((short) 233, ok.value());
        }

        _3:
        {
            assertEquals(114514, props.get("intValue"));
            var result = BeanUtils.getProperty(example, "intValue");
            assertInstanceOf(GetResult.Ok.class, result);
            var ok = (GetResult.Ok) result;
            assertEquals(114514, ok.value());
        }

        _4:
        {
            assertEquals(12345678987654321L, props.get("longValue"));
            var result = BeanUtils.getProperty(example, "longValue");
            assertInstanceOf(GetResult.Ok.class, result);
            var ok = (GetResult.Ok) result;
            assertEquals(12345678987654321L, ok.value());
        }

        _5:
        {
            assertEquals('H', props.get("charValue"));
            var result = BeanUtils.getProperty(example, "charValue");
            assertInstanceOf(GetResult.Ok.class, result);
            var ok = (GetResult.Ok) result;
            assertEquals('H', ok.value());
        }

        _6:
        {
            assertEquals(3.14F, props.get("floatValue"));
            var result = BeanUtils.getProperty(example, "floatValue");
            assertInstanceOf(GetResult.Ok.class, result);
            var ok = (GetResult.Ok) result;
            assertEquals(3.14F, ok.value());
        }

        _7:
        {
            assertEquals(6.02e23, props.get("doubleValue"));
            var result = BeanUtils.getProperty(example, "doubleValue");
            assertInstanceOf(GetResult.Ok.class, result);
            var ok = (GetResult.Ok) result;
            assertEquals(6.02e23, ok.value());
        }

        _8:
        {
            assertEquals(true, props.get("booleanValue"));
            var result = BeanUtils.getProperty(example, "booleanValue");
            assertInstanceOf(GetResult.Ok.class, result);
            var ok = (GetResult.Ok) result;
            assertEquals(true, ok.value());
        }

        _9:
        {
            assertEquals("Hello world", props.get("str"));
            var result = BeanUtils.getProperty(example, "str");
            assertInstanceOf(GetResult.Ok.class, result);
            var ok = (GetResult.Ok) result;
            assertEquals("Hello world", ok.value());
        }

        _10:
        {
            assertIterableEquals(List.of("123", "233"), (List<String>) props.get("strList"));
            var result = BeanUtils.getProperty(example, "strList");
            assertInstanceOf(GetResult.Ok.class, result);
            var ok = (GetResult.Ok) result;
            assertIterableEquals(List.of("123", "233"), (List<String>) ok.value());
        }

        _11:
        {
            assertIterableEquals(List.of(1, "23"), (List<?>) props.get("objList"));
            var result = BeanUtils.getProperty(example, "objList");
            assertInstanceOf(GetResult.Ok.class, result);
            var ok = (GetResult.Ok) result;
            assertIterableEquals(List.of(1, "23"), (List<?>) ok.value());
        }

        _12:
        {
            assertArrayEquals(new int[]{1, 2, 3}, (int[]) props.get("intArr"));
            var result = BeanUtils.getProperty(example, "intArr");
            assertInstanceOf(GetResult.Ok.class, result);
            var ok = (GetResult.Ok) result;
            assertArrayEquals(new int[]{1, 2, 3}, (int[]) ok.value());
        }

        _13:
        {
            assertArrayEquals(new String[]{"123", "233"}, (String[]) props.get("strArr"));
            var result = BeanUtils.getProperty(example, "strArr");
            assertInstanceOf(GetResult.Ok.class, result);
            var ok = (GetResult.Ok) result;
            assertArrayEquals(new String[]{"123", "233"}, (String[]) ok.value());
        }

        _14:
        {
            assertArrayEquals(new Holder[]{new Holder<>("123"), new Holder<>("233")}, (Holder[]) props.get("holderArr"));
            var result = BeanUtils.getProperty(example, "holderArr");
            assertInstanceOf(GetResult.Ok.class, result);
            var ok = (GetResult.Ok) result;
            assertArrayEquals(new Holder[]{new Holder<>("123"), new Holder<>("233")}, (Holder[]) ok.value());
        }
    }

    @Test
    void createRecord() {
        var params = Map.ofEntries(
                /*  1 */ Map.entry("byteValue", (byte) 12),
                /*  2 */ Map.entry("shortValue", (short) 233),
                /*  3 */ Map.entry("intValue", 114514),
                /*  4 */ Map.entry("longValue", 12345678987654321L),
                /*  5 */ Map.entry("charValue", 'H'),
                /*  6 */ Map.entry("floatValue", 3.14F),
                /*  7 */ Map.entry("doubleValue", 6.02e23),
                /*  8 */ Map.entry("booleanValue", true),
                /*  9 */ Map.entry("str", "Hello world"),
                /* 10 */ Map.entry("strList", List.of("123", "233")),
                /* 11 */ Map.entry("objList", List.of(1, "23")),
                /* 12 */ Map.entry("intArr", new int[]{1, 2, 3}),
                /* 13 */ Map.entry("strArr", new String[]{"123", "233"}),
                /* 14 */ Map.entry("holderArr", new Holder[]{new Holder<>("123"), new Holder<>("233")})
        );
        var e = BeanUtils.getBeanInfoFromClass(SingleClassExample.class).create(params);
        assertEquals((byte) 12, e.getByteValue());         /*  1 */
        assertEquals((short) 233, e.getShortValue());      /*  2 */
        assertEquals(114514, e.getIntValue());             /*  3 */
        assertEquals(12345678987654321L, e.getLongValue());/*  4 */
        assertEquals('H', e.getCharValue());               /*  5 */
        assertEquals(3.14F, e.getFloatValue());            /*  6 */
        assertEquals(6.02e23, e.getDoubleValue());         /*  7 */
        assertTrue(e.isBooleanValue());                    /*  8 */
        assertEquals("Hello world", e.getStr());           /*  9 */
        assertIterableEquals(List.of("123", "233"), e.getStrList()); /* 10 */
        assertIterableEquals(List.of(1, "23"), e.getObjList());      /* 11 */
        assertArrayEquals(new int[]{1, 2, 3}, e.getIntArr());        /* 12 */
        assertArrayEquals(new String[]{"123", "233"}, e.getStrArr());/* 13 */
        assertArrayEquals(new Holder[]{new Holder<>("123"), new Holder<>("233")}, e.getHolderArr()); /* 14 */
    }

    @Test
    void createRecordWithNothing() {
        var e = BeanUtils.getBeanInfoFromClass(SingleClassExample.class).create(Map.of());
        assertEquals((byte) 0, e.getByteValue());  /*  1 */
        assertEquals((short) 0, e.getShortValue());/*  2 */
        assertEquals(0, e.getIntValue());          /*  3 */
        assertEquals(0L, e.getLongValue());        /*  4 */
        assertEquals('\0', e.getCharValue());      /*  5 */
        assertEquals(0.0F, e.getFloatValue());     /*  6 */
        assertEquals(0.0, e.getDoubleValue());     /*  7 */
        assertFalse(e.isBooleanValue());           /*  8 */
        assertNull(e.getStr());                    /*  9 */
        assertNull(e.getStrList());                /* 10 */
        assertNull(e.getObjList());                /* 11 */
        assertNull(e.getIntArr());                 /* 12 */
        assertNull(e.getStrArr());                 /* 13 */
        assertNull(e.getHolderArr());              /* 14 */
    }
}
