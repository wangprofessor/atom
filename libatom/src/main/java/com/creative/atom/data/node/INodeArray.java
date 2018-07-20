package com.creative.atom.data.node;

/**
 * Created by wangshouchao on 2017/12/12.
 */

public interface INodeArray extends INodes {
    INode[] getSubNodeArray();
    void arraySetType(int index, INode node);
    INode arrayGetType(int index);
    void arraySetTypeArray(INode[] nodeArray);
    INode[] arrayGetTypeArray();
}
