package com.creation.atom.node.list;

import com.creation.atom.node.BaseCreator;
import com.creation.atom.node.BaseNode;
import com.creation.atom.node.IChild;
import com.creation.atom.node.INode;
import com.creation.atom.node.IParent;

class ListCreator extends BaseCreator {
    @Override
    public INode createNode(Object origin) {
        return new BaseNode(origin, INode.KEY_INT, INode.SIZE_UNFIXED, INode.CLASS_UNBOUNDED);
    }

    @Override
    public IParent createParent(INode[] children) {
        return new ListParent(children);
    }

    @Override
    public IChild createChild(Object key) {
        return new ListChild(key);
    }
}
