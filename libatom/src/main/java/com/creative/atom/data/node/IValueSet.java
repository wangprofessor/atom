package com.creative.atom.data.node;

/**
 * Created by wangshouchao on 2017/12/12.
 */

public interface IValueSet {
    void setArrayValue(INode targetNode, INode sourceNode, Object parent, int index, Object value);
    void setMapValue(INode targetNode, INode sourceNode, Object parent, String name, Object value);
}
