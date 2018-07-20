package com.creative.atom.data.newnode;

public abstract class BaseParent implements IParent {
    private INode node;

    @Override
    public void setNode(INode node) {
        this.node = node;
    }

    @Override
    public INode getNode() {
        return node;
    }
}
