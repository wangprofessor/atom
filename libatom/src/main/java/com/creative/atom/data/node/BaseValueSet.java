package com.creative.atom.data.node;

public class BaseValueSet implements IValueSet {
    @Override
    public void setMapValue(INode targetNode, INode sourceNode, Object parent, String name, Object value) {
        throw new RuntimeException();
    }

    @Override
    public void setArrayValue(INode targetNode, INode sourceNode, Object parent, int index, Object value) {
        throw new RuntimeException();
    }
}
