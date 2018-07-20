package com.creative.atom.data.node;

public interface ISelfSub {
    void selfSetSub(String name, Object value);
    Object selfGetSub(String name);
    void selfSetArraySub(Object array, int index, Object value);
    Object selfGetArraySub(Object array, int index);
}
