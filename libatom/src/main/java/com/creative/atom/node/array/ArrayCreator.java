package com.creative.atom.node.array;

import com.creative.atom.node.BaseCreator;
import com.creative.atom.node.BaseNode;
import com.creative.atom.node.IChild;
import com.creative.atom.node.INode;
import com.creative.atom.node.IParent;

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
