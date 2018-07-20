package com.creative.atom.node.list;

import com.creative.atom.node.INode;
import com.creative.atom.node.IntKeyParent;

import java.util.ArrayList;

class ListParent extends IntKeyParent {
    ListParent(INode[] children) {
        super(children);
    }

    @Override
    public Object createValue(INode sourceNode, Object sourceValue) {
        int size = sourceNode.getParent().getChildren().length;
        return new ArrayList(size);
    }
}
