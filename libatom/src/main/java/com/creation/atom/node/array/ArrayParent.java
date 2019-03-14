package com.creation.atom.node.array;

import com.creation.atom.node.INode;
import com.creation.atom.node.IntKeyParent;

import java.lang.reflect.Array;

class ArrayParent extends IntKeyParent {
    ArrayParent(INode[] children) {
        super(children);
    }

    @Override
    public Object createValue(INode sourceNode, Object sourceValue) {
        int size = sourceNode.getParent().getChildren().length;
        Class<?> clazz = getNode().getClazzBound();
        Class<?> componentType = clazz.getComponentType();
        return Array.newInstance(componentType, size);
    }
}
