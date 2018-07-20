package com.creative.atom.data.newnode;

public class BaseNode implements INode {
    private final Object originValue;
    private final Class<?> clazz;

    private IChild child;
    private IParent parent;

    public BaseNode(Object origin) {
        if (origin == null) {
            throw new RuntimeException();
        }

        Type type = Type.create(origin);
        clazz = type.clazz;
        if (type.isClass) {
            originValue = null;
        } else {
            originValue = origin;
        }
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public boolean hasOriginValue() {
        return originValue != null;
    }

    @Override
    public Object getOriginValue() {
        return originValue;
    }

    @Override
    public boolean isChild() {
        return child != null;
    }

    @Override
    public void setChild(IChild child) {
        this.child = child;
        child.setNode(this);
    }

    @Override
    public IChild getChild() {
        return child;
    }

    @Override
    public boolean isParent() {
        return parent != null;
    }

    @Override
    public void setParent(IParent parent) {
        this.parent = parent;
        parent.setNode(this);
    }

    @Override
    public IParent getParent() {
        return parent;
    }
}
