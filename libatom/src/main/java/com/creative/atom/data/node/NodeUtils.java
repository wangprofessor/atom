package com.creative.atom.data.node;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangshouchao on 2017/12/15.
 */

public class NodeUtils {
    public static boolean isPrimitive(Class<?> clazz) {
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

    public static Class<?> getTypeClass(Object type) {
        Class<?> clazz;
        if(type instanceof Class<?>) {
            clazz = (Class<?>) type;
        } else {
            clazz = type.getClass();
        }
        if(clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        return clazz;
    }

    public static TypeHolder getTypeHolder(Object type) {
        boolean isClass = false;
        Class<?> clazz;
        if(type instanceof Class<?>) {
            clazz = (Class<?>) type;
            isClass = true;
        } else {
            clazz = type.getClass();
        }

        return new TypeHolder(type, clazz, isClass);
    }

    static String getDepthString(int depth) {
        char[] chars = new char[depth];
        Arrays.fill(chars, '.');
        return String.copyValueOf(chars);
    }

    public static INode[] getSplitSubNodes(HashMap<String, INode> nodeMap) {
        INode[] splitSubNodes = new INode[nodeMap.size()];
        int i = 0;
        for (Map.Entry<String, INode> entry : nodeMap.entrySet()) {
            INode node = entry.getValue();
            splitSubNodes[i] = node;
            i++;
        }
        return splitSubNodes;
    }
}
