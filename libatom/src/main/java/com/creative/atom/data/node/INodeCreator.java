package com.creative.atom.data.node;

/**
 * Created by wangshouchao on 2017/12/15.
 */

public interface INodeCreator {
    INodes createRootNodes(Object type);
    INode createSubNode(Object type, String name, String tag, boolean isArraySub, IField field);
    void setNodeHolderClass(Class<?> clazz);
}
