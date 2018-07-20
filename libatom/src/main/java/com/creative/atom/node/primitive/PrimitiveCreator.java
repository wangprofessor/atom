package com.creative.atom.node.primitive;

import com.creative.atom.node.BaseCreator;
import com.creative.atom.node.IChild;
import com.creative.atom.node.INode;
import com.creative.atom.node.IParent;

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
