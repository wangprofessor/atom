package com.creative.atom.data.node;

import java.util.HashMap;

/**
 * Created by wangshouchao on 2017/12/15.
 */
public class NodeHolders {
    private final HashMap<Class<?>, INodeHolder> nodeHolderMap = new HashMap<>();

    public INodeHolder getNodeHolder(Object type) {
        return NodesCreator.getNodeHolder(nodeHolderMap, type);
    }

    public INodeCreator getNodeCreator(Object type) {
        return getNodeHolder(type).getNodeCreator();
    }

    public IValueCreator getValueCreator(Object type) {
        return getNodeHolder(type).getValueCreator();
    }

    public INodeSplit getNodeSplit(Object type) {
        return getNodeHolder(type).getNodeSplit();
    }

    public IValueSet getValueSet(Object type) {
        return getNodeHolder(type).getValueSet();
    }

    public IValueGet getValueGet(Object type) {
        return getNodeHolder(type).getValueGet();
    }
}
