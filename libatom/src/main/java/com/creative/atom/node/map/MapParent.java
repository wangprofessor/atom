package com.creative.atom.node.map;

import com.creative.atom.node.ArrayMap;
import com.creative.atom.node.BaseParent;
import com.creative.atom.node.INode;

import java.util.HashMap;

class MapParent extends BaseParent {
    private final ArrayMap arrayMap;

    MapParent(INode[] children) {
        arrayMap = new ArrayMap(children);
    }

    @Override
    public INode[] getChildren() {
        return arrayMap.getNodeArray();
    }

    @Override
    public INode getChild(Object key) {
        return arrayMap.get((String) key);
    }

    @Override
    public Object createValue(INode sourceNode, Object sourceValue) {
        int size = sourceNode.getParent().getChildren().length;
        return new HashMap(size);
    }
}
