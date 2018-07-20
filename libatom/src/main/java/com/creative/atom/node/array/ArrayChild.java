package com.creative.atom.node.array;

import com.creative.atom.node.BaseChild;

import java.lang.reflect.Array;

class ArrayChild extends BaseChild {
    ArrayChild(Object key) {
        super(key);
    }

    @Override
    public void setValue(Object parentValue, Object value) {
        super.setValue(parentValue, value);
        int index = (int) getKey();
        Array.set(parentValue, index, value);
    }

    @Override
    public Object getValue(Object parentValue) {
        int index = (int) getKey();
        return Array.get(parentValue, index);
    }
}
