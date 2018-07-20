package com.creative.atom.node.object;

import com.creative.atom.node.ArrayMap;
import com.creative.atom.node.BaseParent;
import com.creative.atom.node.INode;

import java.lang.reflect.Array;

public class ObjectParent extends BaseParent {
    private final ArrayMap arrayMap;

    private String name;
    private String soleName;

    ObjectParent(INode[] children) {
        arrayMap = new ArrayMap(children);
    }

    @Override
    public INode[] getChildren() {
        return arrayMap.getNodeArray();
    }

    @Override
    public INode getChild(Object key) {
        return arrayMap.get((String) key);
    }

    @Override
    public Object createValue(INode sourceNode, Object sourceValue) {
        int size = sourceNode.getParent().getChildren().length;
        Class<?> clazz = getNode().getClazz();
        Class<?> componentType = clazz.getComponentType();
        return Array.newInstance(componentType, size);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSoleName(String soleName) {
        this.soleName = soleName;
    }

    public String getSoleName() {
        return soleName;
    }
}
