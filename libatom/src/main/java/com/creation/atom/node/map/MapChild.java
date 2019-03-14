package com.creation.atom.node.map;

import com.creation.atom.node.BaseChild;

import java.util.Map;

class MapChild extends BaseChild {
    MapChild(Object key) {
        super(key);
    }

    @Override
    public void setValue(Object parentValue, Object value) {
        super.setValue(parentValue, value);
        String key = (String) getKey();
        Map map = (Map) parentValue;
        map.put(key, value);
    }

    @Override
    public Object getValue(Object parentValue) {
        String key = (String) getKey();
        Map map = (Map) parentValue;
        return map.get(key);
    }
}
