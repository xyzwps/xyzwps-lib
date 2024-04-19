package com.xyzwps.lib.beans.forold.inherit1;

import com.xyzwps.lib.beans.forold.Point2D;

public class Circle extends Shape {

    private Point2D center;
    private int radius;

    public Point2D getCenter() {
        return center;
    }

    public void setCenter(Point2D center) {
        this.center = center;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
