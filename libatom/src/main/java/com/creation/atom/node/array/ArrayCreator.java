package com.creation.atom.node.array;

import com.creation.atom.node.BaseCreator;
import com.creation.atom.node.BaseNode;
import com.creation.atom.node.IChild;
import com.creation.atom.node.INode;
import com.creation.atom.node.IParent;

class ArrayCreator extends BaseCreator {
    @Override
    public INode createNode(Object origin) {
        return new BaseNode(origin, INode.KEY_INT, INode.SIZE_FIXED, INode.CLASS_BOUNDED);
    }

    @Override
    public IParent createParent(INode[] children) {
        return new ArrayParent(children);
    }

    @Override
    public IChild createChild(Object key) {
        return new ArrayChild(key);
    }
}
