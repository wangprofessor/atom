package com.creative.atom.data.node.object;

import com.creative.atom.data.node.BaseNodeCreator;
import com.creative.atom.data.node.IField;
import com.creative.atom.data.node.INode;

import java.lang.reflect.Array;

/**
 * Created by wangshouchao on 2017/12/15.
 */

class ObjectNodeCreator extends BaseNodeCreator {
    @Override
    protected INode createNode(String name, boolean isArraySub, boolean isClass, Object type, boolean isVariation, String tag, IField field) {
        return new ObjectNode(name, isArraySub, isClass, type, isVariation, tag, field);
    }

    @Override
    protected INode createNodeArray(String name, boolean isArraySub, boolean isClass, Object type, String tag, IField field, int length) {
        return new ObjectNodeArray(name, isArraySub, isClass, type, tag, field, length);
    }

    @Override
    protected INode createNodeMap(String name, boolean isArraySub, boolean isClass, Object type, String tag, IField field) {
        return new ObjectNodeMap(name, isArraySub, isClass, type, tag, field);
    }

    @Override
    protected int getArrayLength(Object type) {
        return Array.getLength(type);
    }
}
