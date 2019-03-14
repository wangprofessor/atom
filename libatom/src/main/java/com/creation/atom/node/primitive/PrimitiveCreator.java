package com.creation.atom.node.primitive;

import com.creation.atom.node.BaseCreator;
import com.creation.atom.node.IChild;
import com.creation.atom.node.INode;
import com.creation.atom.node.IParent;

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
