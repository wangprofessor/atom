package com.creative.atom.data.newnode;

public interface IChild {
    void setNode(INode node);
    INode getNode();

    Object getKey();

    void setValue(Object parentValue, Object value);
    Object getValue(Object parentValue);

    Object getLastSetValue();
}
