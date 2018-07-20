package com.creative.atom.data.node;

/**
 * Created by wangshouchao on 2017/12/12.
 */

public interface INodes extends INode {
    void setIsSplit(boolean isSplit);
    boolean isSplit();
    INode[] getSplitSubNodes();
    void copySubNodesFrom(INodes nodes);

    boolean supportAdd();
}
