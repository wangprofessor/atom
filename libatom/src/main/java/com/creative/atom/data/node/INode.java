package com.creative.atom.data.node;

/**
 * Created by wangshouchao on 2017/12/12.
 */

public interface INode {
    String name();
    void setName(String name);

    boolean isArraySub();
    void setArraySub(boolean isArraySub);

    void setMapToArray(boolean isMapToArray);
    boolean isMapToArray();

    boolean isClass();
    Class<?> getClazz();

    Object getType();

    boolean isVariation();
    void setVariation(boolean isVariation);
    void setVariationNodes(INodes nodes);
    INodes getVariationNodes();
}
