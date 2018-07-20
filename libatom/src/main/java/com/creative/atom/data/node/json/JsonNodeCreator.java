package com.creative.atom.data.node.json;

import com.creative.atom.data.node.BaseNodeArray;
import com.creative.atom.data.node.BaseNodeCreator;
import com.creative.atom.data.node.IField;
import com.creative.atom.data.node.INode;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by wangshouchao on 2017/12/15.
 */

class JsonNodeCreator extends BaseNodeCreator {
    @Override
    protected int getNodeType(Class<?> clazz) {
        if (JSONObject.class.isAssignableFrom(clazz)) {
            return NODES_TYPE_MAP;
        } else if (JSONArray.class.isAssignableFrom(clazz)) {
            return NODES_TYPE_ARRAY;
        } else {
            return NODES_TYPE_UNDEFINE;
        }
    }

    @Override
    protected int getArrayLength(Object type) {
        if (type instanceof JSONArray) {
            return ((JSONArray) type).length();
        }
        throw new RuntimeException();
    }

    @Override
    protected INode createNodeArray(String name, boolean isArraySub, boolean isClass, Object type, String tag, IField field, int length) {
        return new BaseNodeArray(name, type, isArraySub, isClass, true, length);
    }
}
