package com.creative.atom.node;

public interface ISplitter {
    void setCreator(ICreator creator);
    IParent split(INode node);
}
