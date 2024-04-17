package com.xyzwps.lib.beans.forrecord;

import com.xyzwps.lib.beans.BeanUtils;
import com.xyzwps.lib.beans.PropertyInfo;
import com.xyzwps.lib.bedrock.lang.Types;
import org.junit.jupiter.api.Test;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.*;

class AnalyzeRecordTests {

    @Test
    void analyzeRecord() {
        var bi = BeanUtils.getBeanInfo(RecordExample.class);
        var props = bi.getBeanProperties();
        assertEquals(14, props.size());

        for (var prop : props) {
            assertTrue(prop.readable());
            assertFalse(prop.writable());
        }

        IntFunction<PropertyInfo> getProp = (i) -> props.get(i - 1);

        _1:
        {
            var prop = getProp.apply(1);
            assertEquals("byteValue", prop.propertyName());
            assertEquals(byte.class, prop.propertyType());
        }

        _2:
        {
            var prop = getProp.apply(2);
            assertEquals("shortValue", prop.propertyName());
            assertEquals(short.class, prop.propertyType());
        }

        _3:
        {
            var prop = getProp.apply(3);
            assertEquals("intValue", prop.propertyName());
            assertEquals(int.class, prop.propertyType());
        }

        _4:
        {
            var prop = getProp.apply(4);
            assertEquals("longValue", prop.propertyName());
            assertEquals(long.class, prop.propertyType());
        }

        _5:
        {
            var prop = getProp.apply(5);
            assertEquals("charValue", prop.propertyName());
            assertEquals(char.class, prop.propertyType());
        }

        _6:
        {
            var prop = getProp.apply(6);
            assertEquals("floatValue", prop.propertyName());
            assertEquals(float.class, prop.propertyType());
        }

        _7:
        {
            var prop = getProp.apply(7);
            assertEquals("doubleValue", prop.propertyName());
            assertEquals(double.class, prop.propertyType());
        }

        _8:
        {
            var prop = getProp.apply(8);
            assertEquals("booleanValue", prop.propertyName());
            assertEquals(boolean.class, prop.propertyType());
        }

        _9:
        {
            var prop = getProp.apply(9);
            assertEquals("str", prop.propertyName());
            assertEquals(String.class, prop.propertyType());
        }

        _10:
        {
            var prop = getProp.apply(10);
            assertEquals("strList", prop.propertyName());
            assertTrue(Types.isParameterizedType(prop.propertyType()));

            var propType = (ParameterizedType) prop.propertyType();
            assertEquals(List.class, propType.getRawType());
            assertEquals(String.class, propType.getActualTypeArguments()[0]);
        }

        _11:
        {
            var prop = getProp.apply(11);
            assertEquals("objList", prop.propertyName());
            assertTrue(Types.isParameterizedType(prop.propertyType()));

            var propType = (ParameterizedType) prop.propertyType();
            assertEquals(List.class, propType.getRawType());

            var typeArgs0 = propType.getActualTypeArguments()[0];
            assertInstanceOf(WildcardType.class, typeArgs0);
            assertEquals("?", typeArgs0.getTypeName());
        }

        _12:
        {
            var prop = getProp.apply(12);
            assertEquals("intArr", prop.propertyName());
            assertTrue(Types.isClass(prop.propertyType())); // array type is class

            var propType = (Class<?>) prop.propertyType();
            assertEquals(int.class, propType.componentType());
        }

        _13:
        {
            var prop = getProp.apply(13);
            assertEquals("strArr", prop.propertyName());
            assertTrue(Types.isClass(prop.propertyType())); // array type is class

            var propType = (Class<?>) prop.propertyType();
            assertEquals(String.class, propType.componentType());
        }

        _14:
        {
            var prop = getProp.apply(14);
            assertEquals("holderArr", prop.propertyName());
            assertTrue(Types.isGenericArrayType(prop.propertyType())); // array type is class

            var propType = (GenericArrayType) prop.propertyType();
            var compType = propType.getGenericComponentType();
            assertTrue(Types.isParameterizedType(compType));
        }
    }
}
