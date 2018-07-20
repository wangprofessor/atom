package com.creative.atom.data.newnode;

public interface ISplitter {
    void setCreator(ICreator creator);
    IParent split(INode node);
}
