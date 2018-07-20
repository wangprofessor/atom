package com.creative.atom.data.node.map;

import com.creative.atom.data.node.BaseNodeCreator;
import com.creative.atom.data.node.INodeHolder;

import java.lang.reflect.Array;
import java.util.HashMap;

/**
 * Created by wangshouchao on 2017/12/15.
 */

class MapNodeCreator extends BaseNodeCreator {
    @Override
    protected int getNodeType(Class<?> clazz) {
        if (HashMap.class.isAssignableFrom(clazz)) {
            return NODES_TYPE_MAP;
        } else if (clazz.isArray()) {
            return NODES_TYPE_ARRAY;
        } else {
            return NODES_TYPE_UNDEFINE;
        }
    }

    @Override
    protected int getArrayLength(Object type) {
        return Array.getLength(type);
    }
}
