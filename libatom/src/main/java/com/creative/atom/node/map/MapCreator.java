package com.creative.atom.node.map;

import com.creative.atom.node.BaseCreator;
import com.creative.atom.node.BaseNode;
import com.creative.atom.node.IChild;
import com.creative.atom.node.INode;
import com.creative.atom.node.IParent;

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
