package com.creation.atom.node;

public interface ICreator {
    INode createNode(Object origin);
    IParent createParent(INode[] children);
    IChild createChild(Object key);
}
