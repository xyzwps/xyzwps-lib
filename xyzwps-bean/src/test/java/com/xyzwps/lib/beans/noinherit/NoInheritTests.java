package com.xyzwps.lib.beans.noinherit;

import com.xyzwps.lib.beans.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NoInheritTests {

    @Test
    void getAndSet() {
        var person = new Person();
        person.setBirthday(LocalDate.of(2012, 12, 12));
        person.setName("坤坤");
        person.setHeight(165);
        person.setHabits(List.of("篮球", "唱跳", "Rap"));

        assertEquals(165, (Integer) BeanUtils.getPropertyOrNull(person, "height"));
        assertEquals("坤坤", BeanUtils.getPropertyOrNull(person, "name"));
        assertEquals(LocalDate.of(2012, 12, 12), BeanUtils.getPropertyOrNull(person, "birthday"));
        assertIterableEquals(List.of("篮球", "唱跳", "Rap"), BeanUtils.getPropertyOrNull(person, "habits"));

        BeanUtils.setPropertyOrIgnore(person, "height", 226);
        BeanUtils.setPropertyOrIgnore(person, "name", "姚明");
        BeanUtils.setPropertyOrIgnore(person, "birthday", LocalDate.of(1980, 9, 12));
        BeanUtils.setPropertyOrIgnore(person, "habits", List.of("篮球"));

        assertEquals(226, (Integer) BeanUtils.getPropertyOrNull(person, "height"));
        assertEquals("姚明", BeanUtils.getPropertyOrNull(person, "name"));
        assertEquals(LocalDate.of(1980, 9, 12), BeanUtils.getPropertyOrNull(person, "birthday"));
        assertIterableEquals(List.of("篮球"), BeanUtils.getPropertyOrNull(person, "habits"));
    }

    @Test
    void propertyInfo() {
        var person = new Person();
        var beanInfo = BeanUtils.getBeanInfo(person);

        // simple object
        var nameProperty = beanInfo.getPropertyInfo("name")
                .orElseThrow(() -> new RuntimeException("No property name"));
        assertTrue(nameProperty.readable());
        assertTrue(nameProperty.writable());
        assertEquals(String.class, nameProperty.propertyType());
        assertEquals("name", nameProperty.propertyName());

        // primitive
        var heightProperty = beanInfo.getPropertyInfo("height")
                .orElseThrow(() -> new RuntimeException("No property height"));
        assertTrue(heightProperty.readable());
        assertTrue(heightProperty.writable());
        assertEquals(int.class, heightProperty.propertyType());
        assertEquals("height", heightProperty.propertyName());

        // generic
        var habitsProperty = beanInfo.getPropertyInfo("habits")
                .orElseThrow(() -> new RuntimeException("No property habits"));
        assertTrue(habitsProperty.readable());
        assertTrue(habitsProperty.writable());
        assertEquals(List.class, habitsProperty.propertyType());
        assertEquals("habits", habitsProperty.propertyName());
    }

    @Test
    void getProperties() {
        var person = new Person();
        person.setBirthday(LocalDate.of(2012, 12, 12));
        person.setName("坤坤");
        person.setHeight(165);
        person.setHabits(List.of("篮球", "唱", "跳", "Rap"));

        var properties = BeanUtils.getProperties(person);
        assertEquals(properties.size(), 4);
        assertEquals(properties.get("birthday"), LocalDate.of(2012, 12, 12));
        assertEquals(properties.get("name"), "坤坤");
        assertEquals(properties.get("height"), 165);
        assertIterableEquals((List<?>) properties.get("habits"), List.of("篮球", "唱", "跳", "Rap"));
    }

    @Test
    void setPrimitiveWithNull() {
        var person = new Person();
        person.setHeight(100);
        assertEquals(100, (Integer) BeanUtils.getPropertyOrNull(person, "height"));

        var result = BeanUtils.setProperty(person, "height", null);
        assertTrue(result instanceof SetResult.Ok);
        assertEquals(0, (Integer) BeanUtils.getPropertyOrNull(person, "height"));
    }

    @Test
    void noSuchProperty() {
        var person = new Person();
        var beanInfo = BeanUtils.getBeanInfo(person);
        var hahaProperty = beanInfo.getPropertyInfo("haha");
        assertTrue(hahaProperty.isEmpty());

        var setResult = BeanUtils.setProperty(person, "haha", "rua");
        assertTrue(setResult instanceof SetResult.NoSuchProperty);

        var getResult = BeanUtils.getProperty(person, "haha");
        assertTrue(getResult instanceof GetResult.NoSuchProperty);

        BeanUtils.setPropertyOrIgnore(person, "haha", "rua");
        assertNull(BeanUtils.getPropertyOrNull(person, "haha"));
    }


    @Test
    void noClassProperty() {
        var person = new Person();
        var beanInfo = BeanUtils.getBeanInfo(person);
        var classProperty = beanInfo.getPropertyInfo("class");
        assertTrue(classProperty.isEmpty());
    }

    @Test
    void notMatchPropertyValue() {
        var person = new Person();
        person.setName("100");
        assertEquals("100", BeanUtils.getPropertyOrNull(person, "name"));

        var result = BeanUtils.setProperty(person, "name", new Object());
        assertTrue(result instanceof SetResult.Failed);

        BeanUtils.setPropertyOrIgnore(person, "name", new Object());
        assertEquals("100", BeanUtils.getPropertyOrNull(person, "name"));
    }
}
