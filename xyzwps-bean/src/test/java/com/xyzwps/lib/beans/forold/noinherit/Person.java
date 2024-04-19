package com.xyzwps.lib.beans.forold.noinherit;

import java.time.LocalDate;
import java.util.List;

public class Person {
    private String name;
    private int height;
    private LocalDate birthday;
    private List<String> habits;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public List<String> getHabits() {
        return habits;
    }

    public void setHabits(List<String> habits) {
        this.habits = habits;
    }
}
