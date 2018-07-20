package com.creative.atom.data.newnode;

public interface INode {
    Class<?> getClazz();

    boolean hasOriginValue();
    Object getOriginValue();

    boolean isChild();
    void setChild(IChild child);
    IChild getChild();

    boolean isParent();
    void setParent(IParent parent);
    IParent getParent();
}
