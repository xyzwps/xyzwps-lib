package com.xyzwps.lib.beans;

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

        var beanInfo = BeanUtils.getBeanInfo(bean);
        assertEquals(beanInfo.getBeanProperties().size(), 8);

        /* boolean */
        {
            var prop = beanInfo.getPropertyInfo("booleanProp").orElseThrow(unexpected("no booleanProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertFalse(bean.isBooleanProp());
            assertFalse(prop.getPropertyOrThrow(bean, boolean.class));
            assertFalse(prop.getPropertyOrThrow(bean, Boolean.class));
            assertFalse(bean.isBooleanProp());

            prop.setPropertyOrThrow(bean, true);
            assertTrue(bean.isBooleanProp());
            assertTrue(prop.getPropertyOrThrow(bean, boolean.class));
            assertTrue(prop.getPropertyOrThrow(bean, Boolean.class));
            assertTrue(bean.isBooleanProp());

            prop.setPropertyOrThrow(bean, false);
            assertFalse(bean.isBooleanProp());
            assertFalse(prop.getPropertyOrThrow(bean, boolean.class));
            assertFalse(prop.getPropertyOrThrow(bean, Boolean.class));
            assertFalse(bean.isBooleanProp());

            prop.setPropertyOrThrow(bean, Boolean.TRUE);
            assertTrue(bean.isBooleanProp());
            assertTrue(prop.getPropertyOrThrow(bean, boolean.class));
            assertTrue(prop.getPropertyOrThrow(bean, Boolean.class));
            assertTrue(bean.isBooleanProp());

            prop.setPropertyOrThrow(bean, Boolean.FALSE);
            assertFalse(bean.isBooleanProp());
            assertFalse(prop.getPropertyOrThrow(bean, boolean.class));
            assertFalse(prop.getPropertyOrThrow(bean, Boolean.class));
            assertFalse(bean.isBooleanProp());

            prop.setPropertyOrThrow(bean, Boolean.TRUE);
            assertTrue(bean.isBooleanProp());
            prop.setPropertyOrThrow(bean, null);
            assertFalse(bean.isBooleanProp());
            assertFalse(prop.getPropertyOrThrow(bean, boolean.class));
            assertFalse(prop.getPropertyOrThrow(bean, Boolean.class));
            assertFalse(bean.isBooleanProp());

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
            assertFalse(bean.isBooleanProp());
        }

        /* short */
        {
            var prop = beanInfo.getPropertyInfo("shortProp").orElseThrow(unexpected("no shortProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertEquals(0, bean.getShortProp());
            assertEquals(0, (int) prop.getPropertyOrThrow(bean, short.class));
            assertEquals(0, (int) prop.getPropertyOrThrow(bean, Short.class));
            assertEquals(0, bean.getShortProp());

            prop.setPropertyOrThrow(bean, (short) 2);
            assertEquals(2, bean.getShortProp());
            assertEquals(2, (int) prop.getPropertyOrThrow(bean, short.class));
            assertEquals(2, (int) prop.getPropertyOrThrow(bean, Short.class));
            assertEquals(2, bean.getShortProp());

            prop.setPropertyOrThrow(bean, 3);
            assertEquals(3, bean.getShortProp());
            assertEquals(3, (int) prop.getPropertyOrThrow(bean, short.class));
            assertEquals(3, (int) prop.getPropertyOrThrow(bean, Short.class));
            assertEquals(3, bean.getShortProp());

            prop.setPropertyOrThrow(bean, 300L);
            assertEquals(300, bean.getShortProp());
            assertEquals(300, (int) prop.getPropertyOrThrow(bean, short.class));
            assertEquals(300, (int) prop.getPropertyOrThrow(bean, Short.class));
            assertEquals(300, bean.getShortProp());

            prop.setPropertyOrThrow(bean, BigInteger.valueOf(42));
            assertEquals(42, bean.getShortProp());
            assertEquals(42, (int) prop.getPropertyOrThrow(bean, short.class));
            assertEquals(42, (int) prop.getPropertyOrThrow(bean, Short.class));
            assertEquals(42, bean.getShortProp());

            prop.setPropertyOrThrow(bean, BigDecimal.valueOf(43));
            assertEquals(43, bean.getShortProp());
            assertEquals(43, (int) prop.getPropertyOrThrow(bean, short.class));
            assertEquals(43, (int) prop.getPropertyOrThrow(bean, Short.class));
            assertEquals(43, bean.getShortProp());

            prop.setPropertyOrThrow(bean, null);
            assertEquals(0, bean.getShortProp());
            assertEquals(0, (int) prop.getPropertyOrThrow(bean, short.class));
            assertEquals(0, (int) prop.getPropertyOrThrow(bean, Short.class));
            assertEquals(0, bean.getShortProp());

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
            assertEquals(0, bean.getShortProp());
        }

        /* int */
        {
            var prop = beanInfo.getPropertyInfo("intProp").orElseThrow(unexpected("no intProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertEquals(0, bean.getIntProp());
            assertEquals(0, prop.getPropertyOrThrow(bean, int.class));
            assertEquals(0, prop.getPropertyOrThrow(bean, Integer.class));
            assertEquals(0, bean.getIntProp());

            prop.setPropertyOrThrow(bean, (short) 2);
            assertEquals(2, bean.getIntProp());
            assertEquals(2, prop.getPropertyOrThrow(bean, int.class));
            assertEquals(2, prop.getPropertyOrThrow(bean, Integer.class));
            assertEquals(2, bean.getIntProp());

            prop.setPropertyOrThrow(bean, 3);
            assertEquals(3, bean.getIntProp());
            assertEquals(3, prop.getPropertyOrThrow(bean, int.class));
            assertEquals(3, prop.getPropertyOrThrow(bean, Integer.class));
            assertEquals(3, bean.getIntProp());

            prop.setPropertyOrThrow(bean, 300L);
            assertEquals(300, bean.getIntProp());
            assertEquals(300, prop.getPropertyOrThrow(bean, int.class));
            assertEquals(300, prop.getPropertyOrThrow(bean, Integer.class));
            assertEquals(300, bean.getIntProp());

            prop.setPropertyOrThrow(bean, BigInteger.valueOf(42));
            assertEquals(42, bean.getIntProp());
            assertEquals(42, prop.getPropertyOrThrow(bean, int.class));
            assertEquals(42, prop.getPropertyOrThrow(bean, Integer.class));
            assertEquals(42, bean.getIntProp());

            prop.setPropertyOrThrow(bean, BigDecimal.valueOf(43));
            assertEquals(43, bean.getIntProp());
            assertEquals(43, prop.getPropertyOrThrow(bean, int.class));
            assertEquals(43, prop.getPropertyOrThrow(bean, Integer.class));
            assertEquals(43, bean.getIntProp());

            prop.setPropertyOrThrow(bean, null);
            assertEquals(0, bean.getIntProp());
            assertEquals(0, prop.getPropertyOrThrow(bean, int.class));
            assertEquals(0, prop.getPropertyOrThrow(bean, Integer.class));
            assertEquals(0, bean.getIntProp());

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
            assertEquals(0, bean.getIntProp());
        }

        /* long */
        {
            var prop = beanInfo.getPropertyInfo("longProp").orElseThrow(unexpected("no longProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertEquals(0, bean.getLongProp());
            assertEquals(0, prop.getPropertyOrThrow(bean, long.class));
            assertEquals(0, prop.getPropertyOrThrow(bean, Long.class));
            assertEquals(0, bean.getLongProp());

            prop.setPropertyOrThrow(bean, (short) 2);
            assertEquals(2, bean.getLongProp());
            assertEquals(2, prop.getPropertyOrThrow(bean, long.class));
            assertEquals(2, prop.getPropertyOrThrow(bean, Long.class));
            assertEquals(2, bean.getLongProp());

            prop.setPropertyOrThrow(bean, 3);
            assertEquals(3, bean.getLongProp());
            assertEquals(3, prop.getPropertyOrThrow(bean, long.class));
            assertEquals(3, prop.getPropertyOrThrow(bean, Long.class));
            assertEquals(3, bean.getLongProp());

            prop.setPropertyOrThrow(bean, 300L);
            assertEquals(300, bean.getLongProp());
            assertEquals(300, prop.getPropertyOrThrow(bean, long.class));
            assertEquals(300, prop.getPropertyOrThrow(bean, Long.class));
            assertEquals(300, bean.getLongProp());

            prop.setPropertyOrThrow(bean, BigInteger.valueOf(42));
            assertEquals(42, bean.getLongProp());
            assertEquals(42, prop.getPropertyOrThrow(bean, long.class));
            assertEquals(42, prop.getPropertyOrThrow(bean, Long.class));
            assertEquals(42, bean.getLongProp());

            prop.setPropertyOrThrow(bean, BigDecimal.valueOf(43));
            assertEquals(43, bean.getLongProp());
            assertEquals(43, prop.getPropertyOrThrow(bean, long.class));
            assertEquals(43, prop.getPropertyOrThrow(bean, Long.class));
            assertEquals(43, bean.getLongProp());

            prop.setPropertyOrThrow(bean, null);
            assertEquals(0, bean.getLongProp());
            assertEquals(0, prop.getPropertyOrThrow(bean, long.class));
            assertEquals(0, prop.getPropertyOrThrow(bean, Long.class));
            assertEquals(0, bean.getLongProp());

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
            assertEquals(0, bean.getLongProp());
        }

        /* float */
        {
            var prop = beanInfo.getPropertyInfo("floatProp").orElseThrow(unexpected("no floatProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertEquals(0, bean.getFloatProp());
            assertEquals(0, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(0, prop.getPropertyOrThrow(bean, Float.class));
            assertEquals(0, bean.getFloatProp());

            prop.setPropertyOrThrow(bean, (short) 2);
            assertEquals(2, bean.getFloatProp());
            assertEquals(2, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(2, prop.getPropertyOrThrow(bean, Float.class));
            assertEquals(2, bean.getFloatProp());

            prop.setPropertyOrThrow(bean, 3);
            assertEquals(3, bean.getFloatProp());
            assertEquals(3, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(3, prop.getPropertyOrThrow(bean, Float.class));
            assertEquals(3, bean.getFloatProp());

            prop.setPropertyOrThrow(bean, 300L);
            assertEquals(300, bean.getFloatProp());
            assertEquals(300, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(300, prop.getPropertyOrThrow(bean, Float.class));
            assertEquals(300, bean.getFloatProp());

            prop.setPropertyOrThrow(bean, BigInteger.valueOf(42));
            assertEquals(42, bean.getFloatProp());
            assertEquals(42, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(42, prop.getPropertyOrThrow(bean, Float.class));
            assertEquals(42, bean.getFloatProp());

            prop.setPropertyOrThrow(bean, BigDecimal.valueOf(43));
            assertEquals(43, bean.getFloatProp());
            assertEquals(43, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(43, prop.getPropertyOrThrow(bean, Float.class));
            assertEquals(43, bean.getFloatProp());

            prop.setPropertyOrThrow(bean, 30.1f);
            assertEquals(30.1f, bean.getFloatProp());
            assertEquals(30.1f, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(30.1f, prop.getPropertyOrThrow(bean, Float.class));
            assertEquals(30.1f, bean.getFloatProp());

            prop.setPropertyOrThrow(bean, 3.14);
            assertEquals(3.14f, bean.getFloatProp());
            assertEquals(3.14f, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(3.14f, prop.getPropertyOrThrow(bean, Float.class));
            assertEquals(3.14f, bean.getFloatProp());

            prop.setPropertyOrThrow(bean, null);
            assertEquals(0, bean.getFloatProp());
            assertEquals(0, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(0, prop.getPropertyOrThrow(bean, Float.class));
            assertEquals(0, bean.getFloatProp());

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
            assertEquals(0, bean.getFloatProp());
        }

        /* double */
        {
            var prop = beanInfo.getPropertyInfo("doubleProp").orElseThrow(unexpected("no doubleProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertEquals(0, bean.getDoubleProp());
            assertEquals(0, prop.getPropertyOrThrow(bean, double.class));
            assertEquals(0, prop.getPropertyOrThrow(bean, Double.class));
            assertEquals(0, bean.getDoubleProp());

            prop.setPropertyOrThrow(bean, (short) 2);
            assertEquals(2, bean.getDoubleProp());
            assertEquals(2, prop.getPropertyOrThrow(bean, double.class));
            assertEquals(2, prop.getPropertyOrThrow(bean, Double.class));
            assertEquals(2, bean.getDoubleProp());

            prop.setPropertyOrThrow(bean, 3);
            assertEquals(3, bean.getDoubleProp());
            assertEquals(3, prop.getPropertyOrThrow(bean, double.class));
            assertEquals(3, prop.getPropertyOrThrow(bean, Double.class));
            assertEquals(3, bean.getDoubleProp());

            prop.setPropertyOrThrow(bean, 300L);
            assertEquals(300, bean.getDoubleProp());
            assertEquals(300, prop.getPropertyOrThrow(bean, double.class));
            assertEquals(300, prop.getPropertyOrThrow(bean, Double.class));
            assertEquals(300, bean.getDoubleProp());

            prop.setPropertyOrThrow(bean, BigInteger.valueOf(42));
            assertEquals(42, bean.getDoubleProp());
            assertEquals(42, prop.getPropertyOrThrow(bean, double.class));
            assertEquals(42, prop.getPropertyOrThrow(bean, Double.class));
            assertEquals(42, bean.getDoubleProp());

            prop.setPropertyOrThrow(bean, BigDecimal.valueOf(43));
            assertEquals(43, bean.getDoubleProp());
            assertEquals(43, prop.getPropertyOrThrow(bean, double.class));
            assertEquals(43, prop.getPropertyOrThrow(bean, Double.class));
            assertEquals(43, bean.getDoubleProp());

            prop.setPropertyOrThrow(bean, 30.1f);
            assertTrue(Math.abs(30.1 - bean.getDoubleProp()) < 0.001);
            assertTrue(Math.abs(30.1 - prop.getPropertyOrThrow(bean, double.class)) < 0.001);
            assertTrue(Math.abs(30.1 - prop.getPropertyOrThrow(bean, Double.class)) < 0.001);
            assertTrue(Math.abs(30.1 - bean.getDoubleProp()) < 0.001);

            prop.setPropertyOrThrow(bean, 3.14);
            assertEquals(3.14, bean.getDoubleProp());
            assertEquals(3.14, prop.getPropertyOrThrow(bean, double.class));
            assertEquals(3.14, prop.getPropertyOrThrow(bean, Double.class));
            assertEquals(3.14, bean.getDoubleProp());

            prop.setPropertyOrThrow(bean, null);
            assertEquals(0, bean.getDoubleProp());
            assertEquals(0, prop.getPropertyOrThrow(bean, double.class));
            assertEquals(0, prop.getPropertyOrThrow(bean, Double.class));
            assertEquals(0, bean.getDoubleProp());

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
            assertEquals(0, bean.getDoubleProp());
        }

        /* byte */
        {
            var prop = beanInfo.getPropertyInfo("byteProp").orElseThrow(unexpected("no byteProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertEquals((byte) 0, bean.getByteProp());
            assertEquals((byte) 0, prop.getPropertyOrThrow(bean, byte.class));
            assertEquals((byte) 0, prop.getPropertyOrThrow(bean, Byte.class));
            assertEquals((byte) 0, bean.getByteProp());

            prop.setPropertyOrThrow(bean, (byte) 12);
            assertEquals((byte) 12, bean.getByteProp());
            assertEquals((byte) 12, prop.getPropertyOrThrow(bean, byte.class));
            assertEquals((byte) 12, prop.getPropertyOrThrow(bean, Byte.class));
            assertEquals((byte) 12, bean.getByteProp());

            prop.setPropertyOrThrow(bean, null);
            assertEquals((byte) 0, bean.getByteProp());
            assertEquals((byte) 0, prop.getPropertyOrThrow(bean, byte.class));
            assertEquals((byte) 0, prop.getPropertyOrThrow(bean, Byte.class));
            assertEquals((byte) 0, bean.getByteProp());

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
            assertEquals((byte) 0, bean.getByteProp());
        }

        /* char */
        {
            var prop = beanInfo.getPropertyInfo("charProp").orElseThrow(unexpected("no charProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertEquals('\u0000', bean.getCharProp());
            assertEquals('\u0000', prop.getPropertyOrThrow(bean, char.class));
            assertEquals('\u0000', prop.getPropertyOrThrow(bean, Character.class));
            assertEquals('\u0000', bean.getCharProp());

            prop.setPropertyOrThrow(bean, '安');
            assertEquals('安', bean.getCharProp());
            assertEquals('安', prop.getPropertyOrThrow(bean, char.class));
            assertEquals('安', prop.getPropertyOrThrow(bean, Character.class));
            assertEquals('安', bean.getCharProp());

            prop.setPropertyOrThrow(bean, "刻");
            assertEquals('刻', bean.getCharProp());
            assertEquals('刻', prop.getPropertyOrThrow(bean, char.class));
            assertEquals('刻', prop.getPropertyOrThrow(bean, Character.class));
            assertEquals('刻', bean.getCharProp());

            prop.setPropertyOrThrow(bean, null);
            assertEquals('\u0000', bean.getCharProp());
            assertEquals('\u0000', prop.getPropertyOrThrow(bean, char.class));
            assertEquals('\u0000', prop.getPropertyOrThrow(bean, Character.class));
            assertEquals('\u0000', bean.getCharProp());

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
            assertEquals('\u0000', bean.getCharProp());
        }
    }
}
