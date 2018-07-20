package com.creative.atom.node;

/**
 * Created by wangshouchao on 2017/12/17.
 */

public class Type {
    public static Type create(INode node) {
        Object origin;
        if (node.hasOriginValue()) {
            origin = node.getOriginValue();
        } else {
            origin = node.getClazzBound();
        }
        return create(origin);
    }

    public static Type create(Object origin) {
        boolean isClass = false;
        Class<?> clazz;
        if(origin instanceof Class<?>) {
            clazz = (Class<?>) origin;
            isClass = true;
        } else {
            clazz = origin.getClass();
        }

        return new Type(origin, clazz, isClass);
    }

    public final Object origin;
    public final Class<?> clazz;
    public final boolean isClass;

    private Type(Object origin, Class<?> clazz, boolean isClass) {
        this.origin = origin;
        this.clazz = clazz;
        this.isClass = isClass;
    }
}
