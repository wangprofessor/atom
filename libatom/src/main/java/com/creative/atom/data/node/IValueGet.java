package com.creative.atom.data.node;

/**
 * Created by wangshouchao on 2017/12/12.
 */

public interface IValueGet {
    Object getArrayValue(INode node, Object parent, int index);
    Object getMapValue(INode node, Object parent, String name);
}
