package com.creative.atom.data.node;

/**
 * Created by wangshouchao on 2017/12/12.
 */

public class BaseNodeArray extends AbsNodeArray {
    public BaseNodeArray(String name, Object type, boolean isArraySub, boolean isClass, boolean supportAdd, int length) {
        super(new BaseNode(name, isArraySub, isClass, type, false), supportAdd, length);
    }
}
