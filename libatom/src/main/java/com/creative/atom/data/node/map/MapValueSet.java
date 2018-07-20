package com.creative.atom.data.node.map;

import com.creative.atom.data.node.INode;
import com.creative.atom.data.node.IValueSet;

import java.lang.reflect.Array;
import java.util.HashMap;

/**
 * Created by wangshouchao on 2017/12/15.
 */

class MapValueSet implements IValueSet {
    @Override
    public void setMapValue(INode targetNode, INode sourceNode, Object parent, String name, Object value) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) parent;
        hashMap.put(name, value);
    }

    @Override
    public void setArrayValue(INode targetNode, INode sourceNode, Object parent, int index, Object value) {
        Array.set(parent, index, value);
    }
}
