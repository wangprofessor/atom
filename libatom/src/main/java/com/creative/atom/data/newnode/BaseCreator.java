package com.creative.atom.data.newnode;

public abstract class BaseCreator implements ICreator {
    @Override
    public INode createNode(Object origin) {
        return new BaseNode(origin);
    }
}
