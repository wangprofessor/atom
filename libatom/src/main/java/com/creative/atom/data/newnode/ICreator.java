package com.creative.atom.data.newnode;

public interface ICreator {
    INode createNode(Object origin);
    IParent createParent(INode[] children);
    IChild createChild(Object key);
}
