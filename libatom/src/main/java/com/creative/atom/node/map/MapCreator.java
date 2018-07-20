package com.creative.atom.node.map;

import com.creative.atom.node.BaseCreator;
import com.creative.atom.node.IChild;
import com.creative.atom.node.INode;
import com.creative.atom.node.IParent;

class MapCreator extends BaseCreator {
    @Override
    public IParent createParent(INode[] children) {
        return new MapParent(children);
    }

    @Override
    public IChild createChild(Object key) {
        return new MapChild(key);
    }
}
