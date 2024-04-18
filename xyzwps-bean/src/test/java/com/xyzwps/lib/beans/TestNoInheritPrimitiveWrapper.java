package com.xyzwps.lib.beans;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.xyzwps.lib.beans.UnexpectedException.unexpected;
import static org.junit.jupiter.api.Assertions.*;

public class TestNoInheritPrimitiveWrapper {

    @SuppressWarnings("unused")
    public static class DefaultCase {
        private Boolean booleanProp;
        private Short shortProp;
        private Integer integerProp;
        private Long longProp;
        private Float floatProp;
        private Double doubleProp;
        private Byte byteProp;
        private Character characterProp;

        public Boolean getBooleanProp() {
            return booleanProp;
        }

        public void setBooleanProp(Boolean booleanProp) {
            this.booleanProp = booleanProp;
        }

        public Short getShortProp() {
            return shortProp;
        }

        public void setShortProp(Short shortProp) {
            this.shortProp = shortProp;
        }

        public Integer getIntegerProp() {
            return integerProp;
        }

        public void setIntegerProp(Integer integerProp) {
            this.integerProp = integerProp;
        }

        public Long getLongProp() {
            return longProp;
        }

        public void setLongProp(Long longProp) {
            this.longProp = longProp;
        }

        public Float getFloatProp() {
            return floatProp;
        }

        public void setFloatProp(Float floatProp) {
            this.floatProp = floatProp;
        }

        public Double getDoubleProp() {
            return doubleProp;
        }

        public void setDoubleProp(Double doubleProp) {
            this.doubleProp = doubleProp;
        }

        public Byte getByteProp() {
            return byteProp;
        }

        public void setByteProp(Byte byteProp) {
            this.byteProp = byteProp;
        }

        public Character getCharacterProp() {
            return characterProp;
        }

        public void setCharacterProp(Character characterProp) {
            this.characterProp = characterProp;
        }
    }

    @Test
    public void testDefaultCase() {
        var bean = new DefaultCase();

        var beanInfo = BeanUtils.getBeanInfoFromObject(bean);
        assertEquals(beanInfo.getBeanProperties().size(), 8);

//        /* boolean */
//        {
//            var prop = beanInfo.getPropertyInfo("booleanProp").orElseThrow(unexpected("no booleanProp"));
//            assertTrue(prop.readable());
//            assertTrue(prop.writable());
//
//            assertNull(bean.getBooleanProp());
//            assertFalse(prop.getPropertyOrThrow(bean, boolean.class));
//            assertNull(prop.getPropertyOrThrow(bean, Boolean.class));
//            assertNull(bean.getBooleanProp());
//
//            prop.setPropertyOrThrow(bean, true);
//            assertTrue(bean.getBooleanProp());
//            assertTrue(prop.getPropertyOrThrow(bean, boolean.class));
//            assertTrue(prop.getPropertyOrThrow(bean, Boolean.class));
//            assertTrue(bean.getBooleanProp());
//
//            prop.setPropertyOrThrow(bean, false);
//            assertFalse(bean.getBooleanProp());
//            assertFalse(prop.getPropertyOrThrow(bean, boolean.class));
//            assertFalse(prop.getPropertyOrThrow(bean, Boolean.class));
//            assertFalse(bean.getBooleanProp());
//
//            prop.setPropertyOrThrow(bean, Boolean.TRUE);
//            assertTrue(bean.getBooleanProp());
//            assertTrue(prop.getPropertyOrThrow(bean, boolean.class));
//            assertTrue(prop.getPropertyOrThrow(bean, Boolean.class));
//            assertTrue(bean.getBooleanProp());
//
//            prop.setPropertyOrThrow(bean, Boolean.FALSE);
//            assertFalse(bean.getBooleanProp());
//            assertFalse(prop.getPropertyOrThrow(bean, boolean.class));
//            assertFalse(prop.getPropertyOrThrow(bean, Boolean.class));
//            assertFalse(bean.getBooleanProp());
//
//            prop.setPropertyOrThrow(bean, Boolean.TRUE);
//            prop.setPropertyOrThrow(bean, null);
//            assertNull(bean.getBooleanProp());
//            assertFalse(prop.getPropertyOrThrow(bean, boolean.class));
//            assertNull(prop.getPropertyOrThrow(bean, Boolean.class));
//            assertNull(bean.getBooleanProp());
//
//            var result = prop.setProperty(bean, "red");
//            assertTrue(result instanceof SetResult.Failed);
//            var failed = (SetResult.Failed) result;
//            assertTrue(failed.cause() instanceof IllegalArgumentException);
//            assertEquals("argument type mismatch", failed.cause().getMessage());
//            assertNull(bean.getBooleanProp());
//        }
//
//        /* short */
//        {
//            var prop = beanInfo.getPropertyInfo("shortProp").orElseThrow(unexpected("no shortProp"));
//            assertTrue(prop.readable());
//            assertTrue(prop.writable());
//
//            assertNull(bean.getShortProp());
//            assertEquals(0, (int) prop.getPropertyOrThrow(bean, short.class));
//            assertNull(prop.getPropertyOrThrow(bean, Short.class));
//            assertNull(bean.getShortProp());
//
//            prop.setPropertyOrThrow(bean, (short) 2);
//            assertEquals(2, (int) bean.getShortProp());
//            assertEquals(2, (int) prop.getPropertyOrThrow(bean, short.class));
//            assertEquals(2, (int) prop.getPropertyOrThrow(bean, Short.class));
//            assertEquals(2, (int) bean.getShortProp());
//
//            prop.setPropertyOrThrow(bean, 3);
//            assertEquals(3, (int) bean.getShortProp());
//            assertEquals(3, (int) prop.getPropertyOrThrow(bean, short.class));
//            assertEquals(3, (int) prop.getPropertyOrThrow(bean, Short.class));
//            assertEquals(3, (int) bean.getShortProp());
//
//            prop.setPropertyOrThrow(bean, 300L);
//            assertEquals(300, (int) bean.getShortProp());
//            assertEquals(300, (int) prop.getPropertyOrThrow(bean, short.class));
//            assertEquals(300, (int) prop.getPropertyOrThrow(bean, Short.class));
//            assertEquals(300, (int) bean.getShortProp());
//
//            prop.setPropertyOrThrow(bean, BigInteger.valueOf(42));
//            assertEquals(42, (int) bean.getShortProp());
//            assertEquals(42, (int) prop.getPropertyOrThrow(bean, short.class));
//            assertEquals(42, (int) prop.getPropertyOrThrow(bean, Short.class));
//            assertEquals(42, (int) bean.getShortProp());
//
//            prop.setPropertyOrThrow(bean, BigDecimal.valueOf(43));
//            assertEquals(43, (int) bean.getShortProp());
//            assertEquals(43, (int) prop.getPropertyOrThrow(bean, short.class));
//            assertEquals(43, (int) prop.getPropertyOrThrow(bean, Short.class));
//            assertEquals(43, (int) bean.getShortProp());
//
//            prop.setPropertyOrThrow(bean, null);
//            assertNull(bean.getShortProp());
//            assertEquals(0, (int) prop.getPropertyOrThrow(bean, short.class));
//            assertNull(prop.getPropertyOrThrow(bean, Short.class));
//            assertNull(bean.getShortProp());
//
//            var result = prop.setProperty(bean, "red");
//            assertTrue(result instanceof SetResult.Failed);
//            var failed = (SetResult.Failed) result;
//            assertTrue(failed.cause() instanceof IllegalArgumentException);
//            assertEquals("argument type mismatch", failed.cause().getMessage());
//            assertNull(bean.getShortProp());
//        }
//
//        /* integer */
//        {
//            var prop = beanInfo.getPropertyInfo("integerProp").orElseThrow(unexpected("no integerProp"));
//            assertTrue(prop.readable());
//            assertTrue(prop.writable());
//
//            assertNull(bean.getIntegerProp());
//            assertEquals(0, prop.getPropertyOrThrow(bean, int.class));
//            assertNull(prop.getPropertyOrThrow(bean, Integer.class));
//            assertNull(bean.getIntegerProp());
//
//            prop.setPropertyOrThrow(bean, (short) 2);
//            assertEquals(2, bean.getIntegerProp());
//            assertEquals(2, prop.getPropertyOrThrow(bean, int.class));
//            assertEquals(2, prop.getPropertyOrThrow(bean, Integer.class));
//            assertEquals(2, bean.getIntegerProp());
//
//            prop.setPropertyOrThrow(bean, 3);
//            assertEquals(3, bean.getIntegerProp());
//            assertEquals(3, prop.getPropertyOrThrow(bean, int.class));
//            assertEquals(3, prop.getPropertyOrThrow(bean, Integer.class));
//            assertEquals(3, bean.getIntegerProp());
//
//            prop.setPropertyOrThrow(bean, 300L);
//            assertEquals(300, bean.getIntegerProp());
//            assertEquals(300, prop.getPropertyOrThrow(bean, int.class));
//            assertEquals(300, prop.getPropertyOrThrow(bean, Integer.class));
//            assertEquals(300, bean.getIntegerProp());
//
//            prop.setPropertyOrThrow(bean, BigInteger.valueOf(42));
//            assertEquals(42, bean.getIntegerProp());
//            assertEquals(42, prop.getPropertyOrThrow(bean, int.class));
//            assertEquals(42, prop.getPropertyOrThrow(bean, Integer.class));
//            assertEquals(42, bean.getIntegerProp());
//
//            prop.setPropertyOrThrow(bean, BigDecimal.valueOf(43));
//            assertEquals(43, bean.getIntegerProp());
//            assertEquals(43, prop.getPropertyOrThrow(bean, int.class));
//            assertEquals(43, prop.getPropertyOrThrow(bean, Integer.class));
//            assertEquals(43, bean.getIntegerProp());
//
//            prop.setPropertyOrThrow(bean, null);
//            assertNull(bean.getIntegerProp());
//            assertEquals(0, prop.getPropertyOrThrow(bean, int.class));
//            assertNull(prop.getPropertyOrThrow(bean, Integer.class));
//            assertNull(bean.getIntegerProp());
//
//            var result = prop.setProperty(bean, "red");
//            assertTrue(result instanceof SetResult.Failed);
//            var failed = (SetResult.Failed) result;
//            assertTrue(failed.cause() instanceof IllegalArgumentException);
//            assertEquals("argument type mismatch", failed.cause().getMessage());
//            assertNull(bean.getIntegerProp());
//        }
//
//        /* long */
//        {
//            var prop = beanInfo.getPropertyInfo("longProp").orElseThrow(unexpected("no longProp"));
//            assertTrue(prop.readable());
//            assertTrue(prop.writable());
//
//            assertNull(bean.getLongProp());
//            assertEquals(0, prop.getPropertyOrThrow(bean, long.class));
//            assertNull(prop.getPropertyOrThrow(bean, Long.class));
//            assertNull(bean.getLongProp());
//
//            prop.setPropertyOrThrow(bean, (short) 2);
//            assertEquals(2, bean.getLongProp());
//            assertEquals(2, prop.getPropertyOrThrow(bean, long.class));
//            assertEquals(2, prop.getPropertyOrThrow(bean, Long.class));
//            assertEquals(2, bean.getLongProp());
//
//            prop.setPropertyOrThrow(bean, 3);
//            assertEquals(3, bean.getLongProp());
//            assertEquals(3, prop.getPropertyOrThrow(bean, long.class));
//            assertEquals(3, prop.getPropertyOrThrow(bean, Long.class));
//            assertEquals(3, bean.getLongProp());
//
//            prop.setPropertyOrThrow(bean, 300L);
//            assertEquals(300, bean.getLongProp());
//            assertEquals(300, prop.getPropertyOrThrow(bean, long.class));
//            assertEquals(300, prop.getPropertyOrThrow(bean, Long.class));
//            assertEquals(300, bean.getLongProp());
//
//            prop.setPropertyOrThrow(bean, BigInteger.valueOf(42));
//            assertEquals(42, bean.getLongProp());
//            assertEquals(42, prop.getPropertyOrThrow(bean, long.class));
//            assertEquals(42, prop.getPropertyOrThrow(bean, Long.class));
//            assertEquals(42, bean.getLongProp());
//
//            prop.setPropertyOrThrow(bean, BigDecimal.valueOf(43));
//            assertEquals(43, bean.getLongProp());
//            assertEquals(43, prop.getPropertyOrThrow(bean, long.class));
//            assertEquals(43, prop.getPropertyOrThrow(bean, Long.class));
//            assertEquals(43, bean.getLongProp());
//
//            prop.setPropertyOrThrow(bean, null);
//            assertNull(bean.getLongProp());
//            assertEquals(0, prop.getPropertyOrThrow(bean, long.class));
//            assertNull(prop.getPropertyOrThrow(bean, Long.class));
//            assertNull(bean.getLongProp());
//
//            var result = prop.setProperty(bean, "red");
//            assertTrue(result instanceof SetResult.Failed);
//            var failed = (SetResult.Failed) result;
//            assertTrue(failed.cause() instanceof IllegalArgumentException);
//            assertEquals("argument type mismatch", failed.cause().getMessage());
//            assertNull(bean.getLongProp());
//        }
//
//        /* float */
//        {
//            var prop = beanInfo.getPropertyInfo("floatProp").orElseThrow(unexpected("no floatProp"));
//            assertTrue(prop.readable());
//            assertTrue(prop.writable());
//
//            assertNull(bean.getFloatProp());
//            assertEquals(0, prop.getPropertyOrThrow(bean, float.class));
//            assertNull(prop.getPropertyOrThrow(bean, Float.class));
//            assertNull(bean.getFloatProp());
//
//            prop.setPropertyOrThrow(bean, (short) 2);
//            assertEquals(2, bean.getFloatProp());
//            assertEquals(2, prop.getPropertyOrThrow(bean, float.class));
//            assertEquals(2, prop.getPropertyOrThrow(bean, Float.class));
//            assertEquals(2, bean.getFloatProp());
//
//            prop.setPropertyOrThrow(bean, 3);
//            assertEquals(3, bean.getFloatProp());
//            assertEquals(3, prop.getPropertyOrThrow(bean, float.class));
//            assertEquals(3, prop.getPropertyOrThrow(bean, Float.class));
//            assertEquals(3, bean.getFloatProp());
//
//            prop.setPropertyOrThrow(bean, 300L);
//            assertEquals(300, bean.getFloatProp());
//            assertEquals(300, prop.getPropertyOrThrow(bean, float.class));
//            assertEquals(300, prop.getPropertyOrThrow(bean, Float.class));
//            assertEquals(300, bean.getFloatProp());
//
//            prop.setPropertyOrThrow(bean, BigInteger.valueOf(42));
//            assertEquals(42, bean.getFloatProp());
//            assertEquals(42, prop.getPropertyOrThrow(bean, float.class));
//            assertEquals(42, prop.getPropertyOrThrow(bean, Float.class));
//            assertEquals(42, bean.getFloatProp());
//
//            prop.setPropertyOrThrow(bean, BigDecimal.valueOf(43));
//            assertEquals(43, bean.getFloatProp());
//            assertEquals(43, prop.getPropertyOrThrow(bean, float.class));
//            assertEquals(43, prop.getPropertyOrThrow(bean, Float.class));
//            assertEquals(43, bean.getFloatProp());
//
//            prop.setPropertyOrThrow(bean, 30.1f);
//            assertEquals(30.1f, bean.getFloatProp());
//            assertEquals(30.1f, prop.getPropertyOrThrow(bean, float.class));
//            assertEquals(30.1f, prop.getPropertyOrThrow(bean, Float.class));
//            assertEquals(30.1f, bean.getFloatProp());
//
//            prop.setPropertyOrThrow(bean, 3.14);
//            assertEquals(3.14f, bean.getFloatProp());
//            assertEquals(3.14f, prop.getPropertyOrThrow(bean, float.class));
//            assertEquals(3.14f, prop.getPropertyOrThrow(bean, Float.class));
//            assertEquals(3.14f, bean.getFloatProp());
//
//            prop.setPropertyOrThrow(bean, null);
//            assertNull(bean.getFloatProp());
//            assertEquals(0, prop.getPropertyOrThrow(bean, float.class));
//            assertNull(prop.getPropertyOrThrow(bean, Float.class));
//            assertNull(bean.getFloatProp());
//
//            var result = prop.setProperty(bean, "red");
//            assertTrue(result instanceof SetResult.Failed);
//            var failed = (SetResult.Failed) result;
//            assertTrue(failed.cause() instanceof IllegalArgumentException);
//            assertEquals("argument type mismatch", failed.cause().getMessage());
//            assertNull(bean.getFloatProp());
//        }
//
//        /* double */
//        {
//            var prop = beanInfo.getPropertyInfo("doubleProp").orElseThrow(unexpected("no doubleProp"));
//            assertTrue(prop.readable());
//            assertTrue(prop.writable());
//
//            assertNull(bean.getDoubleProp());
//            assertEquals(0, prop.getPropertyOrThrow(bean, double.class));
//            assertNull(prop.getPropertyOrThrow(bean, Double.class));
//            assertNull(bean.getDoubleProp());
//
//            prop.setPropertyOrThrow(bean, (short) 2);
//            assertEquals(2, bean.getDoubleProp());
//            assertEquals(2, prop.getPropertyOrThrow(bean, double.class));
//            assertEquals(2, prop.getPropertyOrThrow(bean, Double.class));
//            assertEquals(2, bean.getDoubleProp());
//
//            prop.setPropertyOrThrow(bean, 3);
//            assertEquals(3, bean.getDoubleProp());
//            assertEquals(3, prop.getPropertyOrThrow(bean, double.class));
//            assertEquals(3, prop.getPropertyOrThrow(bean, Double.class));
//            assertEquals(3, bean.getDoubleProp());
//
//            prop.setPropertyOrThrow(bean, 300L);
//            assertEquals(300, bean.getDoubleProp());
//            assertEquals(300, prop.getPropertyOrThrow(bean, double.class));
//            assertEquals(300, prop.getPropertyOrThrow(bean, Double.class));
//            assertEquals(300, bean.getDoubleProp());
//
//            prop.setPropertyOrThrow(bean, BigInteger.valueOf(42));
//            assertEquals(42, bean.getDoubleProp());
//            assertEquals(42, prop.getPropertyOrThrow(bean, double.class));
//            assertEquals(42, prop.getPropertyOrThrow(bean, Double.class));
//            assertEquals(42, bean.getDoubleProp());
//
//            prop.setPropertyOrThrow(bean, BigDecimal.valueOf(43));
//            assertEquals(43, bean.getDoubleProp());
//            assertEquals(43, prop.getPropertyOrThrow(bean, double.class));
//            assertEquals(43, prop.getPropertyOrThrow(bean, Double.class));
//            assertEquals(43, bean.getDoubleProp());
//
//            prop.setPropertyOrThrow(bean, 30.1f);
//            assertTrue(Math.abs(30.1 - bean.getDoubleProp()) < 0.001);
//            assertTrue(Math.abs(30.1 - prop.getPropertyOrThrow(bean, double.class)) < 0.001);
//            assertTrue(Math.abs(30.1 - prop.getPropertyOrThrow(bean, Double.class)) < 0.001);
//            assertTrue(Math.abs(30.1 - bean.getDoubleProp()) < 0.001);
//
//            prop.setPropertyOrThrow(bean, 3.14);
//            assertEquals(3.14, bean.getDoubleProp());
//            assertEquals(3.14, prop.getPropertyOrThrow(bean, double.class));
//            assertEquals(3.14, prop.getPropertyOrThrow(bean, Double.class));
//            assertEquals(3.14, bean.getDoubleProp());
//
//            prop.setPropertyOrThrow(bean, null);
//            assertNull(bean.getDoubleProp());
//            assertEquals(0, prop.getPropertyOrThrow(bean, double.class));
//            assertNull(prop.getPropertyOrThrow(bean, Double.class));
//            assertNull(bean.getDoubleProp());
//
//            var result = prop.setProperty(bean, "red");
//            assertTrue(result instanceof SetResult.Failed);
//            var failed = (SetResult.Failed) result;
//            assertTrue(failed.cause() instanceof IllegalArgumentException);
//            assertEquals("argument type mismatch", failed.cause().getMessage());
//            assertNull(bean.getDoubleProp());
//        }
//
//        /* byte */
//        {
//            var prop = beanInfo.getPropertyInfo("byteProp").orElseThrow(unexpected("no byteProp"));
//            assertTrue(prop.readable());
//            assertTrue(prop.writable());
//
//            assertNull(bean.getByteProp());
//            assertEquals((byte) 0, prop.getPropertyOrThrow(bean, byte.class));
//            assertNull(prop.getPropertyOrThrow(bean, Byte.class));
//            assertNull(bean.getByteProp());
//
//            prop.setPropertyOrThrow(bean, (byte) 12);
//            assertEquals((byte) 12, bean.getByteProp());
//            assertEquals((byte) 12, prop.getPropertyOrThrow(bean, byte.class));
//            assertEquals((byte) 12, prop.getPropertyOrThrow(bean, Byte.class));
//            assertEquals((byte) 12, bean.getByteProp());
//
//            prop.setPropertyOrThrow(bean, null);
//            assertNull(bean.getByteProp());
//            assertEquals((byte) 0, prop.getPropertyOrThrow(bean, byte.class));
//            assertNull(prop.getPropertyOrThrow(bean, Byte.class));
//            assertNull(bean.getByteProp());
//
//            var result = prop.setProperty(bean, "red");
//            assertTrue(result instanceof SetResult.Failed);
//            var failed = (SetResult.Failed) result;
//            assertTrue(failed.cause() instanceof IllegalArgumentException);
//            assertEquals("argument type mismatch", failed.cause().getMessage());
//            assertNull(bean.getByteProp());
//        }
//
//        /* character */
//        {
//            var prop = beanInfo.getPropertyInfo("characterProp").orElseThrow(unexpected("no characterProp"));
//            assertTrue(prop.readable());
//            assertTrue(prop.writable());
//
//            assertNull(bean.getCharacterProp());
//            assertEquals('\u0000', prop.getPropertyOrThrow(bean, char.class));
//            assertNull(prop.getPropertyOrThrow(bean, Character.class));
//            assertNull(bean.getCharacterProp());
//
//            prop.setPropertyOrThrow(bean, '安');
//            assertEquals('安', bean.getCharacterProp());
//            assertEquals('安', prop.getPropertyOrThrow(bean, char.class));
//            assertEquals('安', prop.getPropertyOrThrow(bean, Character.class));
//            assertEquals('安', bean.getCharacterProp());
//
//            prop.setPropertyOrThrow(bean, "\u0000");
//            assertEquals('\u0000', bean.getCharacterProp());
//            assertEquals('\u0000', prop.getPropertyOrThrow(bean, char.class));
//            assertEquals('\u0000', prop.getPropertyOrThrow(bean, Character.class));
//            assertEquals('\u0000', bean.getCharacterProp());
//
//            prop.setPropertyOrThrow(bean, "刻");
//            assertEquals('刻', bean.getCharacterProp());
//            assertEquals('刻', prop.getPropertyOrThrow(bean, char.class));
//            assertEquals('刻', prop.getPropertyOrThrow(bean, Character.class));
//            assertEquals('刻', bean.getCharacterProp());
//
//            prop.setPropertyOrThrow(bean, null);
//            assertNull(bean.getCharacterProp());
//            assertEquals('\u0000', prop.getPropertyOrThrow(bean, char.class));
//            assertNull(prop.getPropertyOrThrow(bean, Character.class));
//            assertNull(bean.getCharacterProp());
//
//            var result = prop.setProperty(bean, "red");
//            assertTrue(result instanceof SetResult.Failed);
//            var failed = (SetResult.Failed) result;
//            assertTrue(failed.cause() instanceof IllegalArgumentException);
//            assertEquals("argument type mismatch", failed.cause().getMessage());
//            assertNull(bean.getCharacterProp());
//        }
    }
}
