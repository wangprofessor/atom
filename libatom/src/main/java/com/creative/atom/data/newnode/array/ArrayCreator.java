package com.creative.atom.data.newnode.array;

import com.creative.atom.data.newnode.BaseCreator;
import com.creative.atom.data.newnode.IChild;
import com.creative.atom.data.newnode.INode;
import com.creative.atom.data.newnode.IParent;

class ArrayCreator extends BaseCreator {
    @Override
    public IParent createParent(INode[] children) {
        return new ArrayParent(children);
    }

    @Override
    public IChild createChild(Object key) {
        return new ArrayChild(key);
    }
}
