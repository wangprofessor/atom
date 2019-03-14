package com.creation.atom.node.map;

import com.creation.atom.node.INode;
import com.creation.atom.node.StringKeyParent;

import java.util.HashMap;

class MapParent extends StringKeyParent {
    MapParent(INode[] children) {
        super(children);
    }

    @Override
    public Object createValue(INode sourceNode, Object sourceValue) {
        int size = sourceNode.getParent().getChildren().length;
        return new HashMap(size);
    }
}
