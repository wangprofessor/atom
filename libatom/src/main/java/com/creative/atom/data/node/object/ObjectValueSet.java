package com.creative.atom.data.node.object;

import com.creative.atom.data.node.INode;
import com.creative.atom.data.node.IValueSet;

import java.lang.reflect.Array;

/**
 * Created by wangshouchao on 2017/12/15.
 */

class ObjectValueSet implements IValueSet {
    @Override
    public void setMapValue(INode targetNode, INode sourceNode, Object parent, String name, Object value) {
        if(!(targetNode instanceof IObjectNode)) {
            throw new RuntimeException();
        }
        try {
            ((IObjectNode) targetNode).getField().set(parent, value);
        } catch (IllegalAccessException ignored) {}
    }

    @Override
    public void setArrayValue(INode targetNode, INode sourceNode, Object parent, int index, Object value) {
        Array.set(parent, index, value);
    }
}
