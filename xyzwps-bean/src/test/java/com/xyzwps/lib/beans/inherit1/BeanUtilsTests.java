package com.xyzwps.lib.beans.inherit1;

import com.xyzwps.lib.beans.*;
import com.xyzwps.lib.beans.inherit1.Circle;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BeanUtilsTests {

    @Test
    void getAndSet() {
        var circle = new Circle();
        circle.setId("6896730");
        circle.setCenter(new Point2D(1, 2));
        circle.setRadius(5);

        assertEquals(BeanUtils.getPropertyOrNull(circle, "id"), "6896730");
        assertEquals(BeanUtils.getPropertyOrNull(circle, "center"), new Point2D(1, 2));
        assertEquals((Integer) BeanUtils.getPropertyOrNull(circle, "radius"), 5);

        BeanUtils.setPropertyOrIgnore(circle, "id", "zxcvbn");
        BeanUtils.setPropertyOrIgnore(circle, "center", new Point2D(2, 3));
        BeanUtils.setPropertyOrIgnore(circle, "radius", 23);

        assertEquals(BeanUtils.getPropertyOrNull(circle, "id"), "zxcvbn");
        assertEquals(BeanUtils.getPropertyOrNull(circle, "center"), new Point2D(2, 3));
        assertEquals((Integer) BeanUtils.getPropertyOrNull(circle, "radius"), 23);
    }
}
