package com.creative.atom.data.newnode;

public interface IParent {
    void setNode(INode node);
    INode getNode();

    INode[] getChildren();

    INode getChild(Object key);

    Object createValue(INode sourceNode, Object sourceValue);
}
