package com.creation.atom.node;

public abstract class StringKeyParent extends BaseParent {
    private final ArrayMap arrayMap;

    protected StringKeyParent(INode[] children) {
        arrayMap = new ArrayMap(children);
    }

    @Override
    public INode[] getChildren() {
        return arrayMap.getNodeArray();
    }

    @Override
    public INode getChild(Object key) {
        return arrayMap.get((String) key);
    }
}
