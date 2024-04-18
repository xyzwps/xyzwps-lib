package com.xyzwps.lib.beans;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static com.xyzwps.lib.beans.UnexpectedException.unexpected;
import static org.junit.jupiter.api.Assertions.*;

class TestNoInheritObject {

    @Test
    void test() {
        var bean = new ObjectProps();

        var beanInfo = BeanUtils.getBeanInfo(bean);
        assertEquals(beanInfo.getBeanProperties().size(), 2);

//        /* String */
//        {
//            var prop = beanInfo.getPropertyInfo("stringProp").orElseThrow(unexpected("no stringProp"));
//            assertTrue(prop.readable());
//            assertTrue(prop.writable());
//
//            assertNull(bean.getStringProp());
//            assertNull(prop.getPropertyOrThrow(bean, String.class));
//            assertNull(bean.getStringProp());
//
//            prop.setPropertyOrThrow(bean, "");
//            assertEquals("", bean.getStringProp());
//            assertEquals("", prop.getPropertyOrThrow(bean, String.class));
//            assertEquals("", bean.getStringProp());
//
//            prop.setPropertyOrThrow(bean, "aaa");
//            assertEquals("aaa", bean.getStringProp());
//            assertEquals("aaa", prop.getPropertyOrThrow(bean, String.class));
//            assertEquals("aaa", bean.getStringProp());
//
//            prop.setPropertyOrThrow(bean, null);
//            assertNull(bean.getStringProp());
//            assertNull(prop.getPropertyOrThrow(bean, String.class));
//            assertNull(bean.getStringProp());
//
//            prop.setPropertyOrThrow(bean, "null");
//            assertEquals("null", bean.getStringProp());
//            assertEquals("null", prop.getPropertyOrThrow(bean, String.class));
//            assertEquals("null", bean.getStringProp());
//
//            var result = prop.setProperty(bean, LocalDateTime.now());
//            assertTrue(result instanceof SetResult.Failed);
//            var failed = (SetResult.Failed) result;
//            assertTrue(failed.cause() instanceof IllegalArgumentException);
//            assertEquals("argument type mismatch", failed.cause().getMessage());
//
//            assertEquals("null", prop.getPropertyOrThrow(bean, String.class));
//            assertEquals("null", bean.getStringProp());
//        }
//
//        /* java.util.Date  */
//        {
//            var prop = beanInfo.getPropertyInfo("dateProp").orElseThrow(unexpected("no dateProp"));
//            assertTrue(prop.readable());
//            assertTrue(prop.writable());
//
//            assertNull(bean.getDateProp());
//            assertNull(prop.getPropertyOrThrow(bean, Date.class));
//            assertNull(bean.getDateProp());
//
//            /* <-- java.util.Date */
//            {
//                prop.setPropertyOrThrow(bean, new Date(1234567890123L));
//                assertEquals(new Date(1234567890123L), bean.getDateProp());
//                assertEquals(new Date(1234567890123L), prop.getPropertyOrThrow(bean, Date.class));
//                assertEquals(new Date(1234567890123L), bean.getDateProp());
//
//                var result = prop.setProperty(bean, "null");
//                assertTrue(result instanceof SetResult.Failed);
//                var failed = (SetResult.Failed) result;
//                assertTrue(failed.cause() instanceof IllegalArgumentException);
//                assertEquals("argument type mismatch", failed.cause().getMessage());
//
//                assertEquals(new Date(1234567890123L), prop.getPropertyOrThrow(bean, Date.class));
//                assertEquals(new Date(1234567890123L), bean.getDateProp());
//            }
//
//            prop.setPropertyOrThrow(bean, null);
//            assertNull(bean.getDateProp());
//            assertNull(prop.getPropertyOrThrow(bean, Date.class));
//            assertNull(bean.getDateProp());
//
//            /* <-- java.util.Date */
//            {
//                prop.setPropertyOrThrow(bean, new java.sql.Date(22345678L));
//                assertEquals(new java.sql.Date(22345678L), bean.getDateProp());
//                assertEquals(new java.sql.Date(22345678L), prop.getPropertyOrThrow(bean, Date.class));
//                assertEquals(new java.sql.Date(22345678L), bean.getDateProp());
//
//                var result = prop.setProperty(bean, "null");
//                assertTrue(result instanceof SetResult.Failed);
//                var failed = (SetResult.Failed) result;
//                assertTrue(failed.cause() instanceof IllegalArgumentException);
//                assertEquals("argument type mismatch", failed.cause().getMessage());
//
//                assertEquals(new java.sql.Date(22345678L), prop.getPropertyOrThrow(bean, Date.class));
//                assertEquals(new java.sql.Date(22345678L), bean.getDateProp());
//            }
//        }
    }

    @SuppressWarnings("unused")
    public static class ObjectProps {
        private String stringProp;
        private Date dateProp;

        public String getStringProp() {
            return stringProp;
        }

        public void setStringProp(String stringProp) {
            this.stringProp = stringProp;
        }

        public Date getDateProp() {
            return dateProp;
        }

        public void setDateProp(Date dateProp) {
            this.dateProp = dateProp;
        }
    }
}
