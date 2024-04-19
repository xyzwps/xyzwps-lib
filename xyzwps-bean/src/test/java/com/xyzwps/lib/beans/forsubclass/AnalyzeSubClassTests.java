package com.xyzwps.lib.beans.forsubclass;

import com.xyzwps.lib.beans.BeanUtils;
import com.xyzwps.lib.beans.GetResult;
import com.xyzwps.lib.beans.SetResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnalyzeSubClassTests {

    @Test
    void allFromThis() {
        var bi = BeanUtils.getBeanInfoFromClass(ThisExample.class);
        var ex = new ThisExample();

        var prop = bi.getPropertyInfo("thisProp").orElseThrow();
        assertTrue(prop.readable());
        assertTrue(prop.writable());

        var setResult = prop.set(ex, "Haha");
        assertInstanceOf(SetResult.Ok.class, setResult);

        var getResult = prop.get(ex);
        assertInstanceOf(GetResult.Ok.class, getResult);
        assertEquals("Haha", ((GetResult.Ok) getResult).value());
    }

    @Test
    void getterFromThis() {
        var bi = BeanUtils.getBeanInfoFromClass(ThisExample.class);
        var ex = new ThisExample();

        var prop = bi.getPropertyInfo("getterFromThis").orElseThrow();
        assertTrue(prop.readable());
        assertTrue(prop.writable());

        var setResult = prop.set(ex, "Haha");
        assertInstanceOf(SetResult.Ok.class, setResult);

        var getResult = prop.get(ex);
        assertInstanceOf(GetResult.Ok.class, getResult);
        assertEquals("Haha", ((GetResult.Ok) getResult).value());
    }

    @Test
    void setterFromThis() {
        var bi = BeanUtils.getBeanInfoFromClass(ThisExample.class);
        var ex = new ThisExample();

        var prop = bi.getPropertyInfo("setterFromThis").orElseThrow();
        assertTrue(prop.readable());
        assertTrue(prop.writable());

        var setResult = prop.set(ex, "Haha");
        assertInstanceOf(SetResult.Ok.class, setResult);

        var getResult = prop.get(ex);
        assertInstanceOf(GetResult.Ok.class, getResult);
        assertEquals("Haha", ((GetResult.Ok) getResult).value());
    }


    @Test
    void allFromSuper() {
        var bi = BeanUtils.getBeanInfoFromClass(ThisExample.class);
        var ex = new ThisExample();

        var prop = bi.getPropertyInfo("allFromSuper").orElseThrow();
        assertTrue(prop.readable());
        assertTrue(prop.writable());

        var setResult = prop.set(ex, "Haha");
        assertInstanceOf(SetResult.Ok.class, setResult);

        var getResult = prop.get(ex);
        assertInstanceOf(GetResult.Ok.class, getResult);
        assertEquals("Haha", ((GetResult.Ok) getResult).value());
    }

}
