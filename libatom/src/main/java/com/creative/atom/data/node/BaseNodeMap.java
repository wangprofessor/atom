package com.creative.atom.data.node;

/**
 * Created by wangshouchao on 2017/12/12.
 */

public class BaseNodeMap extends AbsNodeMap {
    public BaseNodeMap(String name, Object type, boolean isArraySub, boolean isClass, boolean supportAdd) {
        super(new BaseNode(name, isArraySub, isClass, type, false), supportAdd);
    }
}
