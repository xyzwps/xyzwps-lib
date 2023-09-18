package com.xyzwps.lib.beans;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.beans.UnexpectedException.*;

public class TestNIPrimitive {

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

            assertFalse(prop.getPropertyOrThrow(bean, boolean.class));
            assertFalse(prop.getPropertyOrThrow(bean, Boolean.class));

            prop.setPropertyOrThrow(bean, true);

            assertTrue(prop.getPropertyOrThrow(bean, boolean.class));
            assertTrue(prop.getPropertyOrThrow(bean, Boolean.class));

            // TODO: 胡乱 setProperty
        }

        /* short */
        {
            var prop = beanInfo.getPropertyInfo("shortProp").orElseThrow(unexpected("no shortProp"));
            assertTrue(prop.isReadable());
            assertTrue(prop.isWritable());

            assertEquals(0, (int) prop.getPropertyOrThrow(bean, short.class));
            assertEquals(0, (int) prop.getPropertyOrThrow(bean, Short.class));

            prop.setPropertyOrThrow(bean, (short) 2);

            assertEquals(2, (int) prop.getPropertyOrThrow(bean, short.class));
            assertEquals(2, (int) prop.getPropertyOrThrow(bean, Short.class));

            prop.setPropertyOrThrow(bean, 3);

            assertEquals(3, (int) prop.getPropertyOrThrow(bean, short.class));
            assertEquals(3, (int) prop.getPropertyOrThrow(bean, Short.class));

            // TODO: 胡乱 setProperty
        }

    }
}
