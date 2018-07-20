package com.creative.atom.node;

public abstract class BaseCreator implements ICreator {
    @Override
    public INode createNode(Object origin) {
        return new BaseNode(origin, -1, -1, -1);
    }
}
