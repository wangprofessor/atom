package com.creation.atom.node.list;

import com.creation.atom.node.INode;
import com.creation.atom.node.IntKeyParent;

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
