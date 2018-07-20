package com.creative.atom.node;

public abstract class BaseChild implements IChild {
    private INode node;
    private final Object key;
    private Object lastSetValue;

    protected BaseChild(Object key) {
        this.key = key;
    }

    @Override
    public void setNode(INode node) {
        this.node = node;
    }

    @Override
    public INode getNode() {
        return node;
    }

    @Override
    public Object getKey() {
        return key;
    }

    @Override
    public void setValue(Object parentValue, Object value) {
        lastSetValue = value;
    }

    @Override
    public Object getLastSetValue() {
        return lastSetValue;
    }
}
