package com.creative.atom.data.node;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;

public class BaseValueCreator implements IValueCreator {
    private static final String TAG = "BaseValueCreator";

    @Override
    public Object createArrayValue(Class<?> targetClass, Class<?> sourceClass, int length) {
        Class<?> clazz = getCreateArrayValueClass(targetClass, sourceClass);
        return Array.newInstance(clazz, length);
    }

    @Override
    public Object createMapValue(Class<?> targetClass, Class<?> sourceClass) {
        Class<?> clazz = getCreateMapValueClass(targetClass, sourceClass);
        return createObject(clazz);
    }

    private static Object createObject(Class<?> clazz) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            System.out.println(TAG + " createObject error:" + clazz);
            throw new RuntimeException("empty constructor doesn't exist:" + clazz.getName(), e);
        }
    }

    Class<?> getCreateArrayValueClass(Class<?> targetClass, Class<?> sourceClass) {
        Class<?> arrayClass = targetClass == null ? sourceClass : targetClass;
        if (arrayClass.isArray()) {
            return arrayClass.getComponentType();
        } else {
            return Object.class;
        }
    }

    Class<?> getCreateMapValueClass(Class<?> targetClass, Class<?> sourceClass) {
        if (targetClass == null) {
            return sourceClass;
        }
        if (sourceClass == null) {
            return targetClass;
        }

        Class<?> clazz;
        if (targetClass.isAssignableFrom(sourceClass)) {
            clazz = sourceClass;
        } else {
            clazz = targetClass;
        }

        return clazz;
    }
}
