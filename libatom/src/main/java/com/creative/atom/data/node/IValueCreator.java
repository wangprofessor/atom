package com.creative.atom.data.node;

public interface IValueCreator {
    Object createArrayValue(Class<?> targetClass, Class<?> sourceClass, int length);
    Object createMapValue(Class<?> targetClass, Class<?> sourceClass);
}
