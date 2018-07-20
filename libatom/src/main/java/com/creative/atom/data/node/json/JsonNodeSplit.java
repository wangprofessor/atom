package com.creative.atom.data.node.json;

import com.creative.atom.data.node.INode;
import com.creative.atom.data.node.INodeCreator;
import com.creative.atom.data.node.INodeSplit;
import com.creative.atom.data.node.INodes;
import com.creative.atom.data.node.NodeArrayMap;
import com.creative.atom.data.node.NodeUtils;
import com.creative.atom.data.node.TypeHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by wangshouchao on 2017/12/12.
 */

class JsonNodeSplit implements INodeSplit {
    private final NodeArrayMap<Object, INodes> mSplitTypeList = new NodeArrayMap<>();

    @Override
    public INodes getCacheSplitNodes(Object type) {
        return mSplitTypeList.get(type);
    }

    @Override
    public INode[] splitSubs(Object type, INodes nodes, INodeCreator nodeCreator, int splitType) {
        TypeHolder typeHolder = NodeUtils.getTypeHolder(type);

        if (JSONArray.class.isAssignableFrom(typeHolder.clazz)) {
            INode[] subNodeArray;
            if(typeHolder.isClass) {
                subNodeArray = new INode[0];
            } else {
                JSONArray jsonArray = (JSONArray) type;
                int length = jsonArray.length();
                subNodeArray = new INode[length];
                for (int i = 0; i < length; i++) {
                    Object arraySubType = null;
                    try {
                        arraySubType = jsonArray.get(i);
                    } catch (JSONException ignored) {}
                    subNodeArray[i] = nodeCreator.createSubNode(arraySubType, null, null, true, null);
                }
            }
            return subNodeArray;
        }

        if(typeHolder.isClass) {
            return new INode[0];
        }

        JSONObject jsonType = (JSONObject) type;
        int length = jsonType.length();
        INode[] subNodeArray = new INode[length];
        Iterator<String> iterator = jsonType.keys();

        int i = 0;
        while (iterator.hasNext()) {
            String name = iterator.next();
            Object mapSubType = null;
            try {
                mapSubType = jsonType.get(name);
            } catch (JSONException ignored) {}
            INode subNode = nodeCreator.createSubNode(mapSubType, name, null, false, null);
            subNodeArray[i] = subNode;
            i++;
        }

        mSplitTypeList.put(type, nodes);
        return subNodeArray;
    }
}
