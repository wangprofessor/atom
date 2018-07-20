package com.creative.atom.node.list;

import com.creative.atom.node.BaseCreator;
import com.creative.atom.node.IChild;
import com.creative.atom.node.INode;
import com.creative.atom.node.IParent;

class ListCreator extends BaseCreator {
    @Override
    public IParent createParent(INode[] children) {
        return new ListParent(children);
    }

    @Override
    public IChild createChild(Object key) {
        return new ListChild(key);
    }
}
