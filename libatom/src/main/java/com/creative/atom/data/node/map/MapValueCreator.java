package com.creative.atom.data.node.map;

import com.creative.atom.data.node.BaseValueCreator;
import com.creative.atom.data.node.INodes;

import java.util.HashMap;

class MapValueCreator extends BaseValueCreator {
    @Override
    public Object createMapValue(Class<?> targetClass, Class<?> sourceClass) {
        return new HashMap<String, Object>();
    }
}
