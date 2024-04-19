package com.xyzwps.lib.beans.forgenericclass;

import java.util.List;

public class GenericClassExample<T> {
    private List<T> data;
    private T[] array;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public T[] getArray() {
        return array;
    }

    public void setArray(T[] array) {
        this.array = array;
    }
}
