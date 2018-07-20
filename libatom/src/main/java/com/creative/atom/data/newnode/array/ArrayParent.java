package com.creative.atom.data.newnode.array;

import com.creative.atom.data.newnode.BaseParent;
import com.creative.atom.data.newnode.INode;

import java.lang.reflect.Array;

class ArrayParent extends BaseParent {
    private final INode[] nodeArray;

    ArrayParent(INode[] children) {
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
        Class<?> clazz = getNode().getClazz();
        Class<?> componentType = clazz.getComponentType();
        return Array.newInstance(componentType, size);
    }
}
