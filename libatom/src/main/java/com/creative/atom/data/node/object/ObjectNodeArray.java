package com.creative.atom.data.node.object;

import com.creative.atom.data.node.AbsNodeArray;
import com.creative.atom.data.node.IField;

/**
 * Created by wangshouchao on 2017/12/12.
 */

class ObjectNodeArray extends AbsNodeArray implements IObjectNode {

    ObjectNodeArray(String name, boolean isArraySub, boolean isClass, Object type, String tag, IField field, int length) {
        super(new ObjectNode(name, isArraySub, isClass, type, false, tag, field), false, length);
    }

    @Override
    public IField getField() {
        return ((ObjectNode) mNode).getField();
    }

    @Override
    public void setField(IField field) {
        ((ObjectNode) mNode).setField(field);
    }

    @Override
    public String tag() {
        return ((ObjectNode) mNode).tag();
    }

    @Override
    public void setTag(String tag) {
        ((ObjectNode) mNode).setTag(tag);
    }
}
