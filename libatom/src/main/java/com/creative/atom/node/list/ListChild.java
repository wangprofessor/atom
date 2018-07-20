package com.creative.atom.node.list;

import com.creative.atom.node.BaseChild;

import java.util.List;

class ListChild extends BaseChild {
    ListChild(Object key) {
        super(key);
    }

    @Override
    public void setValue(Object parentValue, Object value) {
        super.setValue(parentValue, value);
        int index = (int) getKey();
        List list = (List) parentValue;
        list.set(index, value);
    }

    @Override
    public Object getValue(Object parentValue) {
        int index = (int) getKey();
        List list = (List) parentValue;
        return list.get(index);
    }
}
