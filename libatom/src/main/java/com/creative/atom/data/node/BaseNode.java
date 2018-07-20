package com.creative.atom.data.node;

/**
 * Created by wangshouchao on 2017/12/12.
 */

public class BaseNode implements INode {
    private String name;
    private boolean isArraySub;
    private final boolean isClass;
    private final Object type;
    private final Class<?> clazz;

    private boolean isVariation;
    private INodes variationNodes;
    private boolean isMapToArray = false;

    public BaseNode(String name, boolean isArraySub, boolean isClass, Object type, boolean isVariation) {
        this.name = name;
        this.isArraySub = isArraySub;
        this.isClass = isClass;
        this.type = type;
        TypeHolder typeHolder = NodeUtils.getTypeHolder(type);
        this.clazz = typeHolder.clazz;
        this.isVariation = isVariation;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isArraySub() {
        return isArraySub;
    }

    @Override
    public void setArraySub(boolean isArraySub) {
        this.isArraySub = isArraySub;
    }

    @Override
    public void setMapToArray(boolean isMapToArray) {
        this.isMapToArray = isMapToArray;
    }

    @Override
    public boolean isMapToArray() {
        return isMapToArray;
    }

    @Override
    public boolean isClass() {
        return isClass;
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public Object getType() {
        return type;
    }

    @Override
    public boolean isVariation() {
        return isVariation;
    }

    @Override
    public void setVariation(boolean isVariation) {
        this.isVariation = isVariation;
    }

    @Override
    public void setVariationNodes(INodes nodes) {
        variationNodes = nodes;
    }

    @Override
    public INodes getVariationNodes() {
        return variationNodes;
    }

    @Override
    public String toString() {
        return "BaseNode{" +
                "name='" + name + '\'' +
                ", isArraySub=" + isArraySub +
                ", isClass=" + isClass +
                ", type=" + type +
                ", clazz=" + clazz +
                ", isVariation=" + isVariation +
                ", variationNodes=" + variationNodes +
                ", mapToArray=" + isMapToArray +
                '}';
    }
}
