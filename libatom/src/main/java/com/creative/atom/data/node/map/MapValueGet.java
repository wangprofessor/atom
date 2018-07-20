package com.creative.atom.data.node.map;

import com.creative.atom.data.node.INode;
import com.creative.atom.data.node.IValueGet;

import java.lang.reflect.Array;
import java.util.HashMap;

/**
 * Created by wangshouchao on 2017/12/15.
 */

class MapValueGet implements IValueGet {
    @Override
    public Object getMapValue(INode node, Object parent, String name) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) parent;
        return hashMap.get(name);
    }

    @Override
    public Object getArrayValue(INode node, Object parent, int index) {
        return Array.get(parent, index);
    }
}
