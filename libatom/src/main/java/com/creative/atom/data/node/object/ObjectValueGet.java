package com.creative.atom.data.node.object;

import com.creative.atom.data.node.INode;
import com.creative.atom.data.node.IValueGet;

import java.lang.reflect.Array;

/**
 * Created by wangshouchao on 2017/12/15.
 */

class ObjectValueGet implements IValueGet {
    @Override
    public Object getMapValue(INode node, Object parent, String name) {
        if(!(node instanceof IObjectNode)) {
            throw new RuntimeException();
        }
        try {
            return ((IObjectNode) node).getField().get(parent);
        } catch (IllegalAccessException ignored) {}
        return null;
    }

    @Override
    public Object getArrayValue(INode node, Object parent, int index) {
        return Array.get(parent, index);
    }
}
