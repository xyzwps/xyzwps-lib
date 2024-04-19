package com.xyzwps.lib.beans.forsubclass;

public class ThisExample extends SuperExample {

    private String thisProp;

    public String getThisProp() {
        return thisProp;
    }

    public void setThisProp(String thisProp) {
        this.thisProp = thisProp;
    }

    public String getGetterFromThis() {
        return super.getterFromThis;
    }

    public void setSetterFromThis(String setterFromThis) {
        super.setterFromThis = setterFromThis;
    }
}
