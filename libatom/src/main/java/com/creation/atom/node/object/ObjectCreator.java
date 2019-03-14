package com.creation.atom.node.object;

import com.creation.atom.node.BaseCreator;
import com.creation.atom.node.BaseNode;
import com.creation.atom.node.IChild;
import com.creation.atom.node.INode;
import com.creation.atom.node.IParent;

class ObjectCreator extends BaseCreator {
    @Override
    public INode createNode(Object origin) {
        return new BaseNode(origin, INode.KEY_STRING, INode.SIZE_FIXED, INode.CLASS_BOUNDED);
    }

    @Override
    public IParent createParent(INode[] children) {
        return new ObjectParent(children);
    }

    @Override
    public IChild createChild(Object key) {
        return new ObjectChild(key);
    }
}
