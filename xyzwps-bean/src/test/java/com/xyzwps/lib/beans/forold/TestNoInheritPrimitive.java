package com.xyzwps.lib.beans.forold;

import com.xyzwps.lib.beans.BeanException;
import com.xyzwps.lib.beans.BeanUtils;
import com.xyzwps.lib.beans.GetResult;
import com.xyzwps.lib.beans.SetResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.beans.UnexpectedException.*;

public class TestNoInheritPrimitive {

    @SuppressWarnings("unused")
    public static class DefaultCase {
        private boolean booleanProp;
        private short shortProp;
        private int intProp;
        private long longProp;
        private float floatProp;
        private double doubleProp;
        private byte byteProp;
        private char charProp;

        public char getCharProp() {
            return charProp;
        }

        public void setCharProp(char charProp) {
            this.charProp = charProp;
        }

        public boolean isBooleanProp() {
            return booleanProp;
        }

        public void setBooleanProp(boolean booleanProp) {
            this.booleanProp = booleanProp;
        }

        public short getShortProp() {
            return shortProp;
        }

        public void setShortProp(short shortProp) {
            this.shortProp = shortProp;
        }

        public int getIntProp() {
            return intProp;
        }

        public void setIntProp(int intProp) {
            this.intProp = intProp;
        }

        public long getLongProp() {
            return longProp;
        }

        public void setLongProp(long longProp) {
            this.longProp = longProp;
        }

        public float getFloatProp() {
            return floatProp;
        }

        public void setFloatProp(float floatProp) {
            this.floatProp = floatProp;
        }

        public double getDoubleProp() {
            return doubleProp;
        }

        public void setDoubleProp(double doubleProp) {
            this.doubleProp = doubleProp;
        }

        public byte getByteProp() {
            return byteProp;
        }

        public void setByteProp(byte byteProp) {
            this.byteProp = byteProp;
        }
    }

    @Test
    public void testDefaultCase() {
        var bean = new DefaultCase();

        var beanInfo = BeanUtils.getBeanInfoFromObject(bean);
        assertEquals(beanInfo.getBeanProperties().size(), 8);

        /* boolean */
        {
            var prop = beanInfo.getPropertyInfo("booleanProp").orElseThrow(unexpected("no booleanProp"));
            assertTrue(prop.readable());
            assertTrue(prop.writable());

            assertFalse(bean.isBooleanProp());
            assertFalse((boolean) ((GetResult.Ok) prop.get(bean)).value());
            assertFalse((boolean) ((GetResult.Ok) prop.get(bean)).value());
            assertFalse(bean.isBooleanProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, true));
            assertTrue(bean.isBooleanProp());
            assertTrue((boolean) ((GetResult.Ok) prop.get(bean)).value());
            assertTrue((boolean) ((GetResult.Ok) prop.get(bean)).value());
            assertTrue(bean.isBooleanProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, false));
            assertFalse(bean.isBooleanProp());
            assertFalse((boolean) ((GetResult.Ok) prop.get(bean)).value());
            assertFalse((boolean) ((GetResult.Ok) prop.get(bean)).value());
            assertFalse(bean.isBooleanProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, Boolean.TRUE));
            assertTrue(bean.isBooleanProp());
            assertTrue((boolean) ((GetResult.Ok) prop.get(bean)).value());
            assertTrue((boolean) ((GetResult.Ok) prop.get(bean)).value());
            assertTrue(bean.isBooleanProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, Boolean.FALSE));
            assertFalse(bean.isBooleanProp());
            assertFalse((boolean) ((GetResult.Ok) prop.get(bean)).value());
            assertFalse((boolean) ((GetResult.Ok) prop.get(bean)).value());
            assertFalse(bean.isBooleanProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, Boolean.TRUE));
            assertTrue(bean.isBooleanProp());
            assertInstanceOf(SetResult.Ok.class, prop.set(bean, null));
            assertFalse(bean.isBooleanProp());
            assertFalse((boolean) ((GetResult.Ok) prop.get(bean)).value());
            assertFalse((boolean) ((GetResult.Ok) prop.get(bean)).value());
            assertFalse(bean.isBooleanProp());

            var result = prop.set(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof BeanException);
            assertEquals("Cannot cast java.lang.String to boolean", failed.cause().getMessage());
            assertFalse(bean.isBooleanProp());
        }

        /* short */
        {
            var prop = beanInfo.getPropertyInfo("shortProp").orElseThrow(unexpected("no shortProp"));
            assertTrue(prop.readable());
            assertTrue(prop.writable());

            assertEquals((short) 0, bean.getShortProp());
            assertEquals((short) 0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals((short) 0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals((short) 0, bean.getShortProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, (short) 2));
            assertEquals((short) 2, bean.getShortProp());
            assertEquals((short) 2, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals((short) 2, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals((short) 2, bean.getShortProp());

            {
                var result = prop.set(bean, 3);
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.lang.Integer to short", failed.cause().getMessage());
                assertEquals(2, bean.getShortProp());
            }

            {
                var result = prop.set(bean, 3L);
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.lang.Long to short", failed.cause().getMessage());
                assertEquals(2, bean.getShortProp());
            }

            {
                var result = prop.set(bean, BigInteger.valueOf(42));
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.math.BigInteger to short", failed.cause().getMessage());
                assertEquals(2, bean.getShortProp());
            }

            {
                var result = prop.set(bean, BigDecimal.valueOf(42));
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.math.BigDecimal to short", failed.cause().getMessage());
                assertEquals(2, bean.getShortProp());
            }

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, null));
            assertEquals((short) 0, bean.getShortProp());
            assertEquals((short) 0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals((short) 0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals((short) 0, bean.getShortProp());

            {
                var result = prop.set(bean, "red");
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.lang.String to short", failed.cause().getMessage());
                assertEquals(0, bean.getShortProp());
            }
        }

        /* int */
        {
            var prop = beanInfo.getPropertyInfo("intProp").orElseThrow(unexpected("no intProp"));
            assertTrue(prop.readable());
            assertTrue(prop.writable());

            assertEquals(0, bean.getIntProp());
            assertEquals(0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0, bean.getIntProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, (short) 2));
            assertEquals(2, bean.getIntProp());
            assertEquals(2, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(2, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(2, bean.getIntProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, 3));
            assertEquals(3, bean.getIntProp());
            assertEquals(3, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(3, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(3, bean.getIntProp());

            {
                var result = prop.set(bean, 300L);
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.lang.Long to int", failed.cause().getMessage());
                assertEquals(3, bean.getIntProp());
            }

            {
                var result = prop.set(bean, BigInteger.valueOf(42));
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.math.BigInteger to int", failed.cause().getMessage());
                assertEquals(3, bean.getIntProp());
            }

            {
                var result = prop.set(bean, BigDecimal.valueOf(42));
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.math.BigDecimal to int", failed.cause().getMessage());
                assertEquals(3, bean.getIntProp());
            }


            assertInstanceOf(SetResult.Ok.class, prop.set(bean, null));
            assertEquals(0, bean.getIntProp());
            assertEquals(0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0, bean.getIntProp());

            {
                var result = prop.set(bean, "red");
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.lang.String to int", failed.cause().getMessage());
                assertEquals(0, bean.getIntProp());
            }
        }

        /* long */
        {
            var prop = beanInfo.getPropertyInfo("longProp").orElseThrow(unexpected("no longProp"));
            assertTrue(prop.readable());
            assertTrue(prop.writable());

            assertEquals(0L, bean.getLongProp());
            assertEquals(0L, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0L, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0L, bean.getLongProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, (short) 2));
            assertEquals(2L, bean.getLongProp());
            assertEquals(2L, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(2L, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(2L, bean.getLongProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, 3));
            assertEquals(3L, bean.getLongProp());
            assertEquals(3L, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(3L, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(3L, bean.getLongProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, 300L));
            assertEquals(300L, bean.getLongProp());
            assertEquals(300L, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(300L, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(300L, bean.getLongProp());

            {
                var result = prop.set(bean, BigInteger.valueOf(42));
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.math.BigInteger to long", failed.cause().getMessage());
                assertEquals(300L, bean.getLongProp());
            }

            {
                var result = prop.set(bean, BigDecimal.valueOf(42));
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.math.BigDecimal to long", failed.cause().getMessage());
                assertEquals(300L, bean.getLongProp());
            }

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, null));
            assertEquals(0L, bean.getLongProp());
            assertEquals(0L, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0L, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0L, bean.getLongProp());

            {
                var result = prop.set(bean, "red");
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.lang.String to long", failed.cause().getMessage());
                assertEquals(0L, bean.getLongProp());
            }
        }

        /* float */
        {
            var prop = beanInfo.getPropertyInfo("floatProp").orElseThrow(unexpected("no floatProp"));
            assertTrue(prop.readable());
            assertTrue(prop.writable());

            assertEquals(0F, bean.getFloatProp());
            assertEquals(0F, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0F, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0F, bean.getFloatProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, (short) 2));
            assertEquals(2F, bean.getFloatProp());
            assertEquals(2F, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(2F, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(2F, bean.getFloatProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, 3));
            assertEquals(3F, bean.getFloatProp());
            assertEquals(3F, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(3F, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(3F, bean.getFloatProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, 300L));
            assertEquals(300F, bean.getFloatProp());
            assertEquals(300F, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(300F, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(300F, bean.getFloatProp());

            {
                var result = prop.set(bean, BigInteger.valueOf(42));
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.math.BigInteger to float", failed.cause().getMessage());
                assertEquals(300F, bean.getFloatProp());
            }

            {
                var result = prop.set(bean, BigDecimal.valueOf(42));
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.math.BigDecimal to float", failed.cause().getMessage());
                assertEquals(300F, bean.getFloatProp());
            }

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, 30.1f));
            assertEquals(30.1f, bean.getFloatProp());
            assertEquals(30.1f, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(30.1f, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(30.1f, bean.getFloatProp());

            {
                var result = prop.set(bean, 42.3);
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.lang.Double to float", failed.cause().getMessage());
                assertEquals(30.1f, bean.getFloatProp());
            }

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, null));
            assertEquals(0F, bean.getFloatProp());
            assertEquals(0F, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0F, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0F, bean.getFloatProp());

            {
                var result = prop.set(bean, "red");
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.lang.String to float", failed.cause().getMessage());
                assertEquals(0, bean.getFloatProp());
            }
        }

        /* double */
        {
            var prop = beanInfo.getPropertyInfo("doubleProp").orElseThrow(unexpected("no doubleProp"));
            assertTrue(prop.readable());
            assertTrue(prop.writable());

            assertEquals(0.0, bean.getDoubleProp());
            assertEquals(0.0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0.0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0.0, bean.getDoubleProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, (short) 2));
            assertEquals(2.0, bean.getDoubleProp());
            assertEquals(2.0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(2.0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(2.0, bean.getDoubleProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, 3));
            assertEquals(3.0, bean.getDoubleProp());
            assertEquals(3.0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(3.0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(3.0, bean.getDoubleProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, 300L));
            assertEquals(300.0, bean.getDoubleProp());
            assertEquals(300.0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(300.0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(300.0, bean.getDoubleProp());

            {
                var result = prop.set(bean, BigInteger.valueOf(42));
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.math.BigInteger to double", failed.cause().getMessage());
                assertEquals(300.0, bean.getDoubleProp());
            }

            {
                var result = prop.set(bean, BigDecimal.valueOf(42));
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.math.BigDecimal to double", failed.cause().getMessage());
                assertEquals(300.0, bean.getDoubleProp());
            }

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, 30.1f));
            assertTrue(Math.abs(30.1 - bean.getDoubleProp()) < 0.001);
            assertTrue(Math.abs(30.1 - (double) (((GetResult.Ok) prop.get(bean)).value())) < 0.001);
            assertTrue(Math.abs(30.1 - (double) (((GetResult.Ok) prop.get(bean)).value())) < 0.001);
            assertTrue(Math.abs(30.1 - bean.getDoubleProp()) < 0.001);

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, 3.14));
            assertEquals(3.14, bean.getDoubleProp());
            assertEquals(3.14, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(3.14, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(3.14, bean.getDoubleProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, null));
            assertEquals(0.0, bean.getDoubleProp());
            assertEquals(0.0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0.0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals(0.0, bean.getDoubleProp());

            {
                var result = prop.set(bean, "red");
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.lang.String to double", failed.cause().getMessage());
                assertEquals(0, bean.getDoubleProp());
            }
        }

        /* byte */
        {
            var prop = beanInfo.getPropertyInfo("byteProp").orElseThrow(unexpected("no byteProp"));
            assertTrue(prop.readable());
            assertTrue(prop.writable());

            assertEquals((byte) 0, bean.getByteProp());
            assertEquals((byte) 0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals((byte) 0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals((byte) 0, bean.getByteProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, (byte) 12));
            assertEquals((byte) 12, bean.getByteProp());
            assertEquals((byte) 12, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals((byte) 12, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals((byte) 12, bean.getByteProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, null));
            assertEquals((byte) 0, bean.getByteProp());
            assertEquals((byte) 0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals((byte) 0, ((GetResult.Ok) prop.get(bean)).value());
            assertEquals((byte) 0, bean.getByteProp());

            {
                var result = prop.set(bean, "red");
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.lang.String to byte", failed.cause().getMessage());
                assertEquals((byte) 0, bean.getByteProp());
            }
        }

        /* char */
        {
            var prop = beanInfo.getPropertyInfo("charProp").orElseThrow(unexpected("no charProp"));
            assertTrue(prop.readable());
            assertTrue(prop.writable());

            assertEquals('\u0000', bean.getCharProp());
            assertEquals('\u0000', ((GetResult.Ok) prop.get(bean)).value());
            assertEquals('\u0000', ((GetResult.Ok) prop.get(bean)).value());
            assertEquals('\u0000', bean.getCharProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, '安'));
            assertEquals('安', bean.getCharProp());
            assertEquals('安', ((GetResult.Ok) prop.get(bean)).value());
            assertEquals('安', ((GetResult.Ok) prop.get(bean)).value());
            assertEquals('安', bean.getCharProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, "刻"));
            assertEquals('刻', bean.getCharProp());
            assertEquals('刻', ((GetResult.Ok) prop.get(bean)).value());
            assertEquals('刻', ((GetResult.Ok) prop.get(bean)).value());
            assertEquals('刻', bean.getCharProp());

            assertInstanceOf(SetResult.Ok.class, prop.set(bean, null));
            assertEquals('\u0000', bean.getCharProp());
            assertEquals('\u0000', ((GetResult.Ok) prop.get(bean)).value());
            assertEquals('\u0000', ((GetResult.Ok) prop.get(bean)).value());
            assertEquals('\u0000', bean.getCharProp());

            {
                var result = prop.set(bean, "red");
                assertTrue(result instanceof SetResult.Failed);
                var failed = (SetResult.Failed) result;
                assertTrue(failed.cause() instanceof BeanException);
                assertEquals("Cannot cast java.lang.String to char", failed.cause().getMessage());
                assertEquals('\u0000', bean.getCharProp());
            }
        }
    }
}
