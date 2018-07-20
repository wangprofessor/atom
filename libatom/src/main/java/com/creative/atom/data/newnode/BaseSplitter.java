package com.creative.atom.data.newnode;

public abstract class BaseSplitter implements ISplitter {
    protected ICreator creator;

    @Override
    public void setCreator(ICreator creator) {
        this.creator = creator;
    }
}
