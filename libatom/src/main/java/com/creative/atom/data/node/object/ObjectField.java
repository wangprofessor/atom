package com.creative.atom.data.node.object;

import com.creative.atom.data.node.IField;

import java.lang.reflect.Field;

class ObjectField implements IField {
    private final Field mField;

    ObjectField(Field field) {
        mField = field;
    }

    @Override
    public void set(Object object, Object value) throws IllegalAccessException {
        mField.set(object, value);
    }

    @Override
    public Object get(Object object) throws IllegalAccessException {
        return mField.get(object);
    }

    @Override
    public Class<?> getType() {
        return mField.getType();
    }

    @Override
    public String getName() {
        return mField.getName();
    }
}
