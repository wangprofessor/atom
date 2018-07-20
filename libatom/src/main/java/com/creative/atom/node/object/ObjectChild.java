package com.creative.atom.node.object;

import com.creative.atom.node.BaseChild;

import java.lang.reflect.Field;

class ObjectChild extends BaseChild {
    private String name;
    private String soleName;

    private Field field;

    ObjectChild(Object key) {
        super(key);
    }

    @Override
    public void setValue(Object parentValue, Object value) {
        super.setValue(parentValue, value);
        try {
            field.set(parentValue, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public Object getValue(Object parentValue) {
        try {
            return field.get(parentValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSoleName(String soleName) {
        this.soleName = soleName;
    }

    public String getSoleName() {
        return soleName;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
