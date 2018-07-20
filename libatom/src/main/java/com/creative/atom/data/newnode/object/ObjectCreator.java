package com.creative.atom.data.newnode.object;

import com.creative.atom.data.newnode.BaseCreator;
import com.creative.atom.data.newnode.BaseNode;
import com.creative.atom.data.newnode.IChild;
import com.creative.atom.data.newnode.INode;
import com.creative.atom.data.newnode.IParent;

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
