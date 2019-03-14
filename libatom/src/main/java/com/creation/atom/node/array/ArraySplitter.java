package com.creation.atom.node.array;

import com.creation.atom.node.BaseSplitter;
import com.creation.atom.node.IChild;
import com.creation.atom.node.INode;
import com.creation.atom.node.IParent;
import com.creation.atom.node.Type;

import java.lang.reflect.Array;

class ArraySplitter extends BaseSplitter {
    @Override
    public IParent split(INode node) {
        Type type = Type.create(node);
        if (type.isClass) {
            return creator.createParent(new INode[0]);
        }

        int size = Array.getLength(type.origin);
        INode[] nodeArray = new INode[size];
        for (int i = 0; i < size; i++) {
            INode childNode;
            Object childValue = Array.get(type.origin, i);
            if (childValue == null) {
                Class<?> componentType = type.clazz.getComponentType();
                childNode = creator.createNode(componentType);
            } else {
                childNode = creator.createNode(childValue);
            }
            IChild child = creator.createChild(i);
            childNode.setChild(child);
            nodeArray[i] = childNode;
        }
        return creator.createParent(nodeArray);
    }
}
