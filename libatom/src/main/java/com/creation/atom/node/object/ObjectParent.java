package com.creation.atom.node.object;

import com.creation.atom.node.INode;
import com.creation.atom.node.StringKeyParent;

import java.lang.reflect.Array;

public class ObjectParent extends StringKeyParent {
    private String name;
    private String soleName;

    ObjectParent(INode[] children) {
        super(children);
    }

    @Override
    public Object createValue(INode sourceNode, Object sourceValue) {
        int size = sourceNode.getParent().getChildren().length;
        Class<?> clazz = getNode().getClazzBound();
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
