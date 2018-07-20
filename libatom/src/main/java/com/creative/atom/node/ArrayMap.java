package com.creative.atom.node;

import java.util.HashMap;

public class ArrayMap {
    private final HashMap<String, Integer> indexMap;
    private final INode[] nodeArray;

    public ArrayMap(INode[] children) {
        indexMap = new HashMap<>(children.length);
        nodeArray = children;
        for (INode childNode : children) {
            Object key = childNode.getChild().getKey();
            add((String) key);
        }
    }

    private void add(String name) {
        int index = indexMap.size();
        indexMap.put(name, index);
    }

    public INode get(String name) {
        return nodeArray[indexMap.get(name)];
    }

    public INode[] getNodeArray() {
        return nodeArray;
    }
}
