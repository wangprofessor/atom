package com.creative.atom.node.list;

import com.creative.atom.node.BaseParent;
import com.creative.atom.node.INode;

import java.util.ArrayList;

class ListParent extends BaseParent {
    private final INode[] nodeArray;

    ListParent(INode[] children) {
        nodeArray = children;
    }

    @Override
    public INode[] getChildren() {
        return nodeArray;
    }

    @Override
    public INode getChild(Object key) {
        int index = (int) key;
        return nodeArray[index];
    }

    @Override
    public Object createValue(INode sourceNode, Object sourceValue) {
        int size = sourceNode.getParent().getChildren().length;
        return new ArrayList(size);
    }
}
