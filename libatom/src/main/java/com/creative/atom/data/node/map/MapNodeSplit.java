package com.creative.atom.data.node.map;

import com.creative.atom.data.node.INode;
import com.creative.atom.data.node.INodeCreator;
import com.creative.atom.data.node.INodeSplit;
import com.creative.atom.data.node.INodes;
import com.creative.atom.data.node.NodeArrayMap;
import com.creative.atom.data.node.NodeUtils;
import com.creative.atom.data.node.TypeHolder;
import com.creative.atom.data.node.object.ObjectNodeHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by wangshouchao on 2017/12/12.
 */

class MapNodeSplit implements INodeSplit {
    private final NodeArrayMap<Object, INodes> mSplitTypeList = new NodeArrayMap<>();

    @Override
    public INodes getCacheSplitNodes(Object type) {
        return mSplitTypeList.get(type);
    }

    @Override
    public INode[] splitSubs(Object type, INodes nodes, INodeCreator nodeCreator, int splitType) {
        TypeHolder typeHolder = NodeUtils.getTypeHolder(type);

        if (typeHolder.clazz.isArray()) {
            return ObjectNodeHolder.getArrayNodes(type, nodeCreator, typeHolder.clazz, typeHolder.isClass);
        }

        if(typeHolder.isClass) {
            return new INode[0];
        }

        HashMap<String, Object> mapType = (HashMap<String, Object>) type;
        Set<Map.Entry<String, Object>> entrySet = mapType.entrySet();
        int length = entrySet.size();
        INode[] subNodeArray = new INode[length];
        int i = 0;
        for(Map.Entry<String, Object> entry : entrySet) {
            String name = entry.getKey();
            Object mapSubType = entry.getValue();
            INode subNode = nodeCreator.createSubNode(mapSubType, name, null, false, null);
            subNodeArray[i] = subNode;
            i++;
        }
        mSplitTypeList.put(type, nodes);
        return subNodeArray;
    }
}
