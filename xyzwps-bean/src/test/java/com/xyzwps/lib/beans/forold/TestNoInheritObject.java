package com.xyzwps.lib.beans.forold;

import com.xyzwps.lib.beans.BeanUtils;
import com.xyzwps.lib.beans.GetResult;
import com.xyzwps.lib.beans.SetResult;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TestNoInheritObject {

    @Test
    void test() {
        var bean = new ObjectProps();

        var beanInfo = BeanUtils.getBeanInfoFromObject(bean);
        assertEquals(beanInfo.getBeanProperties().size(), 2);

        /* String */
        {
            var prop = beanInfo.getPropertyInfo("stringProp").orElseThrow();
            assertTrue(prop.readable());
            assertTrue(prop.writable());

            assertNull(bean.getStringProp());
            assertNull(((GetResult.Ok) prop.get(bean)).value());
            assertNull(bean.getStringProp());

            {
                var setResult = prop.set(bean, "");
                assertInstanceOf(SetResult.Ok.class, setResult);
                assertEquals("", bean.getStringProp());
                var getResult = prop.get(bean);
                assertEquals("", ((GetResult.Ok) getResult).value());
                assertEquals("", bean.getStringProp());
            }

            {
                var setResult = prop.set(bean, "aaa");
                assertInstanceOf(SetResult.Ok.class, setResult);
                assertEquals("aaa", bean.getStringProp());
                var getResult = prop.get(bean);
                assertEquals("aaa", ((GetResult.Ok) getResult).value());
                assertEquals("aaa", bean.getStringProp());
            }

            {
                var setResult = prop.set(bean, null);
                assertInstanceOf(SetResult.Ok.class, setResult);
                assertNull(bean.getStringProp());
                var getResult = prop.get(bean);
                assertNull(((GetResult.Ok) getResult).value());
                assertNull(bean.getStringProp());
            }

            {
                var setResult = prop.set(bean, "null");
                assertInstanceOf(SetResult.Ok.class, setResult);
                assertEquals("null", bean.getStringProp());
                var getResult = prop.get(bean);
                assertEquals("null", ((GetResult.Ok) getResult).value());
                assertEquals("null", bean.getStringProp());
            }
            {
                var setResult = prop.set(bean, LocalDateTime.now());
                assertInstanceOf(SetResult.Failed.class, setResult);
                var failed = (SetResult.Failed) setResult;
                assertInstanceOf(IllegalArgumentException.class, failed.cause());
                assertEquals("argument type mismatch", failed.cause().getMessage());

                assertEquals("null", ((GetResult.Ok) prop.get(bean)).value());
                assertEquals("null", bean.getStringProp());
            }
        }

        /* java.util.Date  */
        {
            var prop = beanInfo.getPropertyInfo("dateProp").orElseThrow();
            assertTrue(prop.readable());
            assertTrue(prop.writable());

            assertNull(bean.getDateProp());
            assertNull(((GetResult.Ok) prop.get(bean)).value());
            assertNull(bean.getDateProp());

            /* <-- java.util.Date */
            {
                var setResult = prop.set(bean, new Date(1234567890123L));
                assertInstanceOf(SetResult.Ok.class, setResult);
                assertEquals(new Date(1234567890123L), bean.getDateProp());
                assertEquals(new Date(1234567890123L), ((GetResult.Ok) prop.get(bean)).value());
                assertEquals(new Date(1234567890123L), bean.getDateProp());
            }

            {
                var setResult = prop.set(bean, "null");
                assertInstanceOf(SetResult.Failed.class, setResult);
                var failed = (SetResult.Failed) setResult;
                assertInstanceOf(IllegalArgumentException.class, failed.cause());
                assertEquals("argument type mismatch", failed.cause().getMessage());

                assertEquals(new Date(1234567890123L), ((GetResult.Ok) prop.get(bean)).value());
                assertEquals(new Date(1234567890123L), bean.getDateProp());
            }

            {
                var setResult = prop.set(bean, null);
                assertInstanceOf(SetResult.Ok.class, setResult);
                assertNull(bean.getDateProp());
                assertNull(((GetResult.Ok) prop.get(bean)).value());
                assertNull(bean.getDateProp());
            }

            /* <-- java.util.Date */
            {
                var setResult = prop.set(bean, new java.sql.Date(22345678L));
                assertInstanceOf(SetResult.Ok.class, setResult);
                assertEquals(new java.sql.Date(22345678L), bean.getDateProp());
                assertEquals(new java.sql.Date(22345678L), ((GetResult.Ok) prop.get(bean)).value());
                assertEquals(new java.sql.Date(22345678L), bean.getDateProp());

            }

            {
                var setResult = prop.set(bean, "null");
                assertInstanceOf(SetResult.Failed.class, setResult);
                var failed = (SetResult.Failed) setResult;
                assertInstanceOf(IllegalArgumentException.class, failed.cause());
                assertEquals("argument type mismatch", failed.cause().getMessage());

                assertEquals(new java.sql.Date(22345678L), ((GetResult.Ok) prop.get(bean)).value());
                assertEquals(new java.sql.Date(22345678L), bean.getDateProp());
            }
        }
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
