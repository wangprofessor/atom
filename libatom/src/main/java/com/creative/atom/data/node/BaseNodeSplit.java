package com.creative.atom.data.node;

public class BaseNodeSplit implements INodeSplit {
    @Override
    public INodes getCacheSplitNodes(Object type) {
        return null;
    }

    @Override
    public INode[] splitSubs(Object type, INodes nodes, INodeCreator nodeCreator, int splitType) {
        return new INode[0];
    }
}
