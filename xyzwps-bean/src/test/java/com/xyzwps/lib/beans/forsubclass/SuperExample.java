package com.xyzwps.lib.beans.forsubclass;

public class SuperExample {
    protected String getterFromThis;
    protected String setterFromThis;

    private String allFromSuper;

    public void setGetterFromThis(String getterFromThis) {
        this.getterFromThis = getterFromThis;
    }

    public String getSetterFromThis() {
        return setterFromThis;
    }

    public String getAllFromSuper() {
        return allFromSuper;
    }

    public void setAllFromSuper(String allFromSuper) {
        this.allFromSuper = allFromSuper;
    }
}
