package com.creative.atom.node;

public abstract class BaseSplitter implements ISplitter {
    protected ICreator creator;

    @Override
    public void setCreator(ICreator creator) {
        this.creator = creator;
    }
}
