package com.creative.atom.data.node;

/**
 * Created by wangshouchao on 2017/12/17.
 */

public class TypeHolder {
    public final Object type;
    public final Class<?> clazz;
    public final boolean isClass;

    public TypeHolder(Object type, Class<?> clazz, boolean isClass) {
        this.type = type;
        this.clazz = clazz;
        this.isClass = isClass;
    }
}
