package com.creative.atom.node.array;

import com.creative.atom.node.INode;
import com.creative.atom.node.IntKeyParent;

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
