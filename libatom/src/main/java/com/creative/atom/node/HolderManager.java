package com.creative.atom.node;

import com.creative.atom.node.array.ArrayHolder;
import com.creative.atom.node.primitive.PrimitiveHolder;

import java.util.HashMap;

public class HolderManager {
    public static final IHolder sPrimitiveHolder = new PrimitiveHolder();

    private static final IHolder sArrayHolder = new ArrayHolder();
    private static final HashMap<Class<?>, IHolder> sHolderMap = new HashMap<>();

    static {
        initPrimitive();
    }

    public static boolean isClassPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() ||
                String.class.isAssignableFrom(clazz) ||
                Integer.class.isAssignableFrom(clazz) ||
                Long.class.isAssignableFrom(clazz) ||
                Boolean.class.isAssignableFrom(clazz) ||
                Float.class.isAssignableFrom(clazz) ||
                Double.class.isAssignableFrom(clazz) ||
                Character.class.isAssignableFrom(clazz) ||
                Byte.class.isAssignableFrom(clazz) ||
                Short.class.isAssignableFrom(clazz);
    }

    private static void initPrimitive() {
        sHolderMap.put(String.class, null);

        sHolderMap.put(Integer.class, null);
        sHolderMap.put(Long.class, null);
        sHolderMap.put(Boolean.class, null);
        sHolderMap.put(Float.class, null);
        sHolderMap.put(Double.class, null);
        sHolderMap.put(Character.class, null);
        sHolderMap.put(Byte.class, null);
        sHolderMap.put(Short.class, null);
    }

    public static IHolder getHolder(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return null;
        }
        if (clazz.isArray()) {
            return sArrayHolder;
        }
        return sHolderMap.get(clazz);
    }
}
