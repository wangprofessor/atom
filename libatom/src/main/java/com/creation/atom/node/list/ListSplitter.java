package com.creation.atom.node.list;

import com.creation.atom.node.BaseSplitter;
import com.creation.atom.node.IChild;
import com.creation.atom.node.INode;
import com.creation.atom.node.IParent;
import com.creation.atom.node.Type;

import java.util.List;

class ListSplitter extends BaseSplitter {
    @Override
    public IParent split(INode node) {
        Type type = Type.create(node);
        if (type.isClass) {
            return creator.createParent(new INode[0]);
        }

        List list = (List) type.origin;
        int size = list.size();
        INode[] nodeArray = new INode[size];
        for (int i = 0; i < size; i++) {
            INode childNode;
            Object childValue = list.get(i);
            if (childValue == null) {
                nodeArray[i] = null;
            } else {
                childNode = creator.createNode(childValue);
                IChild child = creator.createChild(i);
                childNode.setChild(child);
                nodeArray[i] = childNode;
            }
        }
        return creator.createParent(nodeArray);
    }
}
