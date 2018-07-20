package com.creative.atom.data.node;

/**
 * Created by wangshouchao on 2017/12/15.
 */

public interface INodeHolder {
    INodeHolder copy();
    INodeCreator getNodeCreator();
    IValueCreator getValueCreator();
    INodeSplit getNodeSplit();
    IValueSet getValueSet();
    IValueGet getValueGet();
}
