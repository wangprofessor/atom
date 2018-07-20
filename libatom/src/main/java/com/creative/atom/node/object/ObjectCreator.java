package com.creative.atom.node.object;

import com.creative.atom.node.BaseCreator;
import com.creative.atom.node.BaseNode;
import com.creative.atom.node.IChild;
import com.creative.atom.node.INode;
import com.creative.atom.node.IParent;

class ObjectCreator extends BaseCreator {
    @Override
    public INode createNode(Object origin) {
        return new BaseNode(origin);
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
