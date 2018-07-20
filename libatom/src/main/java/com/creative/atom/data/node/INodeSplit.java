package com.creative.atom.data.node;

/**
 * Created by wangshouchao on 2017/12/12.
 */

public interface INodeSplit {
    INodes getCacheSplitNodes(Object type);
    INode[] splitSubs(Object type, INodes nodes, INodeCreator nodeCreator, int splitType);
}
