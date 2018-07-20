package com.creative.atom.data.node;

public interface IField {
    void set(Object object, Object value) throws IllegalAccessException;
    Object get(Object object) throws IllegalAccessException;
    Class<?> getType();
    String getName();
}
