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

        var beanInfo = BeanUtils.getBeanInfo(bean);
        assertEquals(beanInfo.getBeanProperties().size(), 8);

        /* boolean */
        {
            var prop = beanInfo.getPropertyInfo("booleanProp").orElseThrow(unexpected("no booleanProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertFalse(prop.getPropertyOrThrow(bean, boolean.class));
            assertNull(prop.getPropertyOrThrow(bean, Boolean.class));

            prop.setPropertyOrThrow(bean, true);
            assertTrue(prop.getPropertyOrThrow(bean, boolean.class));
            assertTrue(prop.getPropertyOrThrow(bean, Boolean.class));

            prop.setPropertyOrThrow(bean, false);
            assertFalse(prop.getPropertyOrThrow(bean, boolean.class));
            assertFalse(prop.getPropertyOrThrow(bean, Boolean.class));

            prop.setPropertyOrThrow(bean, Boolean.TRUE);
            assertTrue(prop.getPropertyOrThrow(bean, boolean.class));
            assertTrue(prop.getPropertyOrThrow(bean, Boolean.class));

            prop.setPropertyOrThrow(bean, Boolean.FALSE);
            assertFalse(prop.getPropertyOrThrow(bean, boolean.class));
            assertFalse(prop.getPropertyOrThrow(bean, Boolean.class));

            prop.setPropertyOrThrow(bean, Boolean.TRUE);
            prop.setPropertyOrThrow(bean, null);
            assertFalse(prop.getPropertyOrThrow(bean, boolean.class));
            assertNull(prop.getPropertyOrThrow(bean, Boolean.class));

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
        }

        /* short */
        {
            var prop = beanInfo.getPropertyInfo("shortProp").orElseThrow(unexpected("no shortProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertEquals(0, (int) prop.getPropertyOrThrow(bean, short.class));
            assertNull(prop.getPropertyOrThrow(bean, Short.class));

            prop.setPropertyOrThrow(bean, (short) 2);
            assertEquals(2, (int) prop.getPropertyOrThrow(bean, short.class));
            assertEquals(2, (int) prop.getPropertyOrThrow(bean, Short.class));

            prop.setPropertyOrThrow(bean, 3);
            assertEquals(3, (int) prop.getPropertyOrThrow(bean, short.class));
            assertEquals(3, (int) prop.getPropertyOrThrow(bean, Short.class));

            prop.setPropertyOrThrow(bean, 300L);
            assertEquals(300, (int) prop.getPropertyOrThrow(bean, short.class));
            assertEquals(300, (int) prop.getPropertyOrThrow(bean, Short.class));

            prop.setPropertyOrThrow(bean, BigInteger.valueOf(42));
            assertEquals(42, (int) prop.getPropertyOrThrow(bean, short.class));
            assertEquals(42, (int) prop.getPropertyOrThrow(bean, Short.class));

            prop.setPropertyOrThrow(bean, BigDecimal.valueOf(43));
            assertEquals(43, (int) prop.getPropertyOrThrow(bean, short.class));
            assertEquals(43, (int) prop.getPropertyOrThrow(bean, Short.class));

            prop.setPropertyOrThrow(bean, null);
            assertEquals(0, (int) prop.getPropertyOrThrow(bean, short.class));
            assertNull(prop.getPropertyOrThrow(bean, Short.class));

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
        }

        /* integer */
        {
            var prop = beanInfo.getPropertyInfo("integerProp").orElseThrow(unexpected("no shortProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertEquals(0, prop.getPropertyOrThrow(bean, int.class));
            assertNull(prop.getPropertyOrThrow(bean, Integer.class));

            prop.setPropertyOrThrow(bean, (short) 2);
            assertEquals(2, prop.getPropertyOrThrow(bean, int.class));
            assertEquals(2, prop.getPropertyOrThrow(bean, Integer.class));

            prop.setPropertyOrThrow(bean, 3);
            assertEquals(3, prop.getPropertyOrThrow(bean, int.class));
            assertEquals(3, prop.getPropertyOrThrow(bean, Integer.class));

            prop.setPropertyOrThrow(bean, 300L);
            assertEquals(300, prop.getPropertyOrThrow(bean, int.class));
            assertEquals(300, prop.getPropertyOrThrow(bean, Integer.class));

            prop.setPropertyOrThrow(bean, BigInteger.valueOf(42));
            assertEquals(42, prop.getPropertyOrThrow(bean, int.class));
            assertEquals(42, prop.getPropertyOrThrow(bean, Integer.class));

            prop.setPropertyOrThrow(bean, BigDecimal.valueOf(43));
            assertEquals(43, prop.getPropertyOrThrow(bean, int.class));
            assertEquals(43, prop.getPropertyOrThrow(bean, Integer.class));

            prop.setPropertyOrThrow(bean, null);
            assertEquals(0, prop.getPropertyOrThrow(bean, int.class));
            assertNull(prop.getPropertyOrThrow(bean, Integer.class));

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
        }

        /* long */
        {
            var prop = beanInfo.getPropertyInfo("longProp").orElseThrow(unexpected("no shortProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertEquals(0, prop.getPropertyOrThrow(bean, long.class));
            assertNull(prop.getPropertyOrThrow(bean, Long.class));

            prop.setPropertyOrThrow(bean, (short) 2);
            assertEquals(2, prop.getPropertyOrThrow(bean, long.class));
            assertEquals(2, prop.getPropertyOrThrow(bean, Long.class));

            prop.setPropertyOrThrow(bean, 3);
            assertEquals(3, prop.getPropertyOrThrow(bean, long.class));
            assertEquals(3, prop.getPropertyOrThrow(bean, Long.class));

            prop.setPropertyOrThrow(bean, 300L);
            assertEquals(300, prop.getPropertyOrThrow(bean, long.class));
            assertEquals(300, prop.getPropertyOrThrow(bean, Long.class));

            prop.setPropertyOrThrow(bean, BigInteger.valueOf(42));
            assertEquals(42, prop.getPropertyOrThrow(bean, long.class));
            assertEquals(42, prop.getPropertyOrThrow(bean, Long.class));

            prop.setPropertyOrThrow(bean, BigDecimal.valueOf(43));
            assertEquals(43, prop.getPropertyOrThrow(bean, long.class));
            assertEquals(43, prop.getPropertyOrThrow(bean, Long.class));

            prop.setPropertyOrThrow(bean, null);
            assertEquals(0, prop.getPropertyOrThrow(bean, long.class));
            assertNull(prop.getPropertyOrThrow(bean, Long.class));

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
        }

        /* float */
        {
            var prop = beanInfo.getPropertyInfo("floatProp").orElseThrow(unexpected("no shortProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertEquals(0, prop.getPropertyOrThrow(bean, float.class));
            assertNull(prop.getPropertyOrThrow(bean, Float.class));

            prop.setPropertyOrThrow(bean, (short) 2);
            assertEquals(2, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(2, prop.getPropertyOrThrow(bean, Float.class));

            prop.setPropertyOrThrow(bean, 3);
            assertEquals(3, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(3, prop.getPropertyOrThrow(bean, Float.class));

            prop.setPropertyOrThrow(bean, 300L);
            assertEquals(300, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(300, prop.getPropertyOrThrow(bean, Float.class));

            prop.setPropertyOrThrow(bean, BigInteger.valueOf(42));
            assertEquals(42, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(42, prop.getPropertyOrThrow(bean, Float.class));

            prop.setPropertyOrThrow(bean, BigDecimal.valueOf(43));
            assertEquals(43, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(43, prop.getPropertyOrThrow(bean, Float.class));

            prop.setPropertyOrThrow(bean, 30.1f);
            assertEquals(30.1f, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(30.1f, prop.getPropertyOrThrow(bean, Float.class));

            prop.setPropertyOrThrow(bean, 3.14);
            assertEquals(3.14f, prop.getPropertyOrThrow(bean, float.class));
            assertEquals(3.14f, prop.getPropertyOrThrow(bean, Float.class));

            prop.setPropertyOrThrow(bean, null);
            assertEquals(0, prop.getPropertyOrThrow(bean, float.class));
            assertNull(prop.getPropertyOrThrow(bean, Float.class));

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
        }

        /* double */
        {
            var prop = beanInfo.getPropertyInfo("doubleProp").orElseThrow(unexpected("no shortProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertEquals(0, prop.getPropertyOrThrow(bean, double.class));
            assertNull(prop.getPropertyOrThrow(bean, Double.class));

            prop.setPropertyOrThrow(bean, (short) 2);
            assertEquals(2, prop.getPropertyOrThrow(bean, double.class));
            assertEquals(2, prop.getPropertyOrThrow(bean, Double.class));

            prop.setPropertyOrThrow(bean, 3);
            assertEquals(3, prop.getPropertyOrThrow(bean, double.class));
            assertEquals(3, prop.getPropertyOrThrow(bean, Double.class));

            prop.setPropertyOrThrow(bean, 300L);
            assertEquals(300, prop.getPropertyOrThrow(bean, double.class));
            assertEquals(300, prop.getPropertyOrThrow(bean, Double.class));

            prop.setPropertyOrThrow(bean, BigInteger.valueOf(42));
            assertEquals(42, prop.getPropertyOrThrow(bean, double.class));
            assertEquals(42, prop.getPropertyOrThrow(bean, Double.class));

            prop.setPropertyOrThrow(bean, BigDecimal.valueOf(43));
            assertEquals(43, prop.getPropertyOrThrow(bean, double.class));
            assertEquals(43, prop.getPropertyOrThrow(bean, Double.class));

            prop.setPropertyOrThrow(bean, 30.1f);
            assertTrue(Math.abs(30.1 - prop.getPropertyOrThrow(bean, double.class)) < 0.001);
            assertTrue(Math.abs(30.1 - prop.getPropertyOrThrow(bean, Double.class)) < 0.001);

            prop.setPropertyOrThrow(bean, 3.14);
            assertEquals(3.14, prop.getPropertyOrThrow(bean, double.class));
            assertEquals(3.14, prop.getPropertyOrThrow(bean, Double.class));

            prop.setPropertyOrThrow(bean, null);
            assertEquals(0, prop.getPropertyOrThrow(bean, double.class));
            assertNull(prop.getPropertyOrThrow(bean, Double.class));

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
        }

        /* byte */
        {
            var prop = beanInfo.getPropertyInfo("byteProp").orElseThrow(unexpected("no shortProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertEquals((byte) 0, prop.getPropertyOrThrow(bean, byte.class));
            assertNull(prop.getPropertyOrThrow(bean, Byte.class));

            prop.setPropertyOrThrow(bean, (byte) 12);
            assertEquals((byte) 12, prop.getPropertyOrThrow(bean, byte.class));
            assertEquals((byte) 12, prop.getPropertyOrThrow(bean, Byte.class));

            prop.setPropertyOrThrow(bean, null);
            assertEquals((byte) 0, prop.getPropertyOrThrow(bean, byte.class));
            assertNull(prop.getPropertyOrThrow(bean, Byte.class));

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
        }

        /* character */
        {
            var prop = beanInfo.getPropertyInfo("characterProp").orElseThrow(unexpected("no shortProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertEquals('\u0000', prop.getPropertyOrThrow(bean, char.class));
            assertNull(prop.getPropertyOrThrow(bean, Character.class));

            prop.setPropertyOrThrow(bean, '安');
            assertEquals('安', prop.getPropertyOrThrow(bean, char.class));
            assertEquals('安', prop.getPropertyOrThrow(bean, Character.class));

            prop.setPropertyOrThrow(bean, "刻");
            assertEquals('刻', prop.getPropertyOrThrow(bean, char.class));
            assertEquals('刻', prop.getPropertyOrThrow(bean, Character.class));

            prop.setPropertyOrThrow(bean, null);
            assertEquals('\u0000', prop.getPropertyOrThrow(bean, char.class));
            assertNull(prop.getPropertyOrThrow(bean, Character.class));

            var result = prop.setProperty(bean, "red");
            assertTrue(result instanceof SetResult.Failed);
            var failed = (SetResult.Failed) result;
            assertTrue(failed.cause() instanceof IllegalArgumentException);
            assertEquals("argument type mismatch", failed.cause().getMessage());
        }
    }
}
