package com.creative.atom.data.node;

public class BaseValueGet implements IValueGet {
    @Override
    public Object getMapValue(INode node, Object parent, String name) {
        throw new RuntimeException();
    }

    @Override
    public Object getArrayValue(INode node, Object parent, int index) {
        throw new RuntimeException();
    }
}
