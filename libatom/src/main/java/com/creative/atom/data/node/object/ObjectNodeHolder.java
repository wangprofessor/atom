package com.creative.atom.data.node.object;

import com.creative.atom.data.node.BaseNodeHolder;
import com.creative.atom.data.node.BaseValueCreator;
import com.creative.atom.data.node.INode;
import com.creative.atom.data.node.INodeCreator;
import com.creative.atom.data.node.INodeHolder;

import java.lang.reflect.Array;

/**
 * Created by wangshouchao on 2017/12/15.
 */

public class ObjectNodeHolder extends BaseNodeHolder {
    public ObjectNodeHolder() {
        super(
                new ObjectNodeCreator(),
                new BaseValueCreator(),
                new ObjectNodeSplit(),
                new ObjectValueSet(),
                new ObjectValueGet()
        );
    }

    @Override
    public INodeHolder copy() {
        return new ObjectNodeHolder();
    }

    public static INode[] getArrayNodes(Object type, INodeCreator nodeCreator, Class<?> clazz, boolean isClass) {
        INode[] subNodeArray;
        if(isClass) {
            subNodeArray = new INode[1];
            Class<?> arraySubType = clazz.getComponentType();
            subNodeArray[0] = nodeCreator.createSubNode(arraySubType, null, null, true, null);
        } else {
            int length = Array.getLength(type);
            subNodeArray = new INode[length];
            for (int i = 0; i < length; i++) {
                Object arraySubType = Array.get(type, i);
                subNodeArray[i] = nodeCreator.createSubNode(arraySubType, null, null, true, null);
            }
        }
        return subNodeArray;
    }
}
