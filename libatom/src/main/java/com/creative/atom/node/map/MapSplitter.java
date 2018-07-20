package com.creative.atom.node.map;

import com.creative.atom.node.BaseSplitter;
import com.creative.atom.node.IChild;
import com.creative.atom.node.INode;
import com.creative.atom.node.IParent;
import com.creative.atom.node.Type;

import java.util.List;
import java.util.Map;

class MapSplitter extends BaseSplitter {
    @Override
    public IParent split(INode node) {
        Type type = Type.create(node);
        if (type.isClass) {
            return creator.createParent(new INode[0]);
        }

        Map<String, Object> list = (Map) type.origin;
        int size = list.size();
        INode[] nodeArray = new INode[size];
        int i = 0;
        for (Map.Entry<String, Object> entry : list.entrySet()) {
            String key = entry.getKey();
            Object childValue = entry.getValue();
            INode childNode;
            if (childValue == null) {
                nodeArray[i] = null;
            } else {
                childNode = creator.createNode(childValue);
                IChild child = creator.createChild(key);
                childNode.setChild(child);
                nodeArray[i] = childNode;
            }
            i++;
        }
        return creator.createParent(nodeArray);
    }
}
