package com.creation.atom.node;

public abstract class IntKeyParent extends BaseParent {
    private final INode[] nodeArray;

    protected IntKeyParent(INode[] children) {
        nodeArray = children;
    }

    @Override
    public INode[] getChildren() {
        return nodeArray;
    }

    @Override
    public INode getChild(Object key) {
        int index = (int) key;
        return nodeArray[index];
    }
}
