package com.creative.atom.data.newnode.primitive;

import com.creative.atom.data.newnode.BaseCreator;
import com.creative.atom.data.newnode.IChild;
import com.creative.atom.data.newnode.INode;
import com.creative.atom.data.newnode.IParent;

public class PrimitiveCreator extends BaseCreator {
    @Override
    public IParent createParent(INode[] children) {
        return null;
    }

    @Override
    public IChild createChild(Object key) {
        throw new RuntimeException();
    }
}
