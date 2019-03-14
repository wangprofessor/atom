package com.creation.atom.node.map;

import com.creation.atom.node.BaseCreator;
import com.creation.atom.node.BaseNode;
import com.creation.atom.node.IChild;
import com.creation.atom.node.INode;
import com.creation.atom.node.IParent;

class MapCreator extends BaseCreator {
    @Override
    public INode createNode(Object origin) {
        return new BaseNode(origin, INode.KEY_STRING, INode.SIZE_UNFIXED, INode.CLASS_UNBOUNDED);
    }

    @Override
    public IParent createParent(INode[] children) {
        return new MapParent(children);
    }

    @Override
    public IChild createChild(Object key) {
        return new MapChild(key);
    }
}
