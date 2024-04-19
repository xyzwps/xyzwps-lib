package com.xyzwps.lib.beans.forsingleclass;

import com.xyzwps.lib.beans.forold.Holder;

import java.util.List;

public class SingleClassExample {
    /*  1 */ private byte byteValue;
    /*  2 */ private short shortValue;
    /*  3 */ private int intValue;
    /*  4 */ private long longValue;
    /*  5 */ private char charValue;
    /*  6 */ private float floatValue;
    /*  7 */ private double doubleValue;
    /*  8 */ private boolean booleanValue;
    /*  9 */ private String str;
    /* 10 */ private List<String> strList;
    /* 11 */ private List<?> objList;
    /* 12 */ private int[] intArr;
    /* 13 */ private String[] strArr;
    /* 14 */ private Holder<String>[] holderArr;

    public byte getByteValue() {
        return byteValue;
    }

    public void setByteValue(byte byteValue) {
        this.byteValue = byteValue;
    }

    public short getShortValue() {
        return shortValue;
    }

    public void setShortValue(short shortValue) {
        this.shortValue = shortValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public char getCharValue() {
        return charValue;
    }

    public void setCharValue(char charValue) {
        this.charValue = charValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public List<String> getStrList() {
        return strList;
    }

    public void setStrList(List<String> strList) {
        this.strList = strList;
    }

    public List<?> getObjList() {
        return objList;
    }

    public void setObjList(List<?> objList) {
        this.objList = objList;
    }

    public int[] getIntArr() {
        return intArr;
    }

    public void setIntArr(int[] intArr) {
        this.intArr = intArr;
    }

    public String[] getStrArr() {
        return strArr;
    }

    public void setStrArr(String[] strArr) {
        this.strArr = strArr;
    }

    public Holder<String>[] getHolderArr() {
        return holderArr;
    }

    public void setHolderArr(Holder<String>[] holderArr) {
        this.holderArr = holderArr;
    }
}
