package com.creative.atom.data.node.object;

import com.creative.atom.data.node.INode;
import com.creative.atom.data.node.INodeCreator;
import com.creative.atom.data.node.INodeSplit;
import com.creative.atom.data.node.INodes;
import com.creative.atom.data.node.NodeUtils;
import com.creative.atom.data.node.TypeHolder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by wangshouchao on 2017/12/12.
 */

class ObjectNodeSplit implements INodeSplit {
    private static final HashMap<Class<?>, INodes> mSplitNodesMap = new HashMap<>();

    @Override
    public INodes getCacheSplitNodes(Object type) {
        TypeHolder typeHolder = NodeUtils.getTypeHolder(type);
        return mSplitNodesMap.get(typeHolder.clazz);
    }

    @Override
    public INode[] splitSubs(Object type, INodes nodes, INodeCreator nodeCreator, int splitType) {
        TypeHolder typeHolder = NodeUtils.getTypeHolder(type);
        Class<?> clazz = typeHolder.clazz;

        if(clazz.isArray()) {
            return ObjectNodeHolder.getArrayNodes(type, nodeCreator, clazz, typeHolder.isClass);
        }

        ArrayList<SubField> subFields = getFieldArray(clazz);
        int size = subFields.size();

        boolean isMapToArray = nodes.isMapToArray();
        if (isMapToArray && size != 1) {
            throw new RuntimeException();
        }

        ArrayList<INode> subNodeArray = new ArrayList<>();
        boolean fieldIsPrimitive = true;
        for (int i = 0; i < size; i++) {
            SubField subField = subFields.get(i);
            Class<?> parentClass = subField.field.getDeclaringClass();
            boolean annotation = isAnnotation(parentClass);

            String name;
            String tag;

            if (annotation) {
                Node node = subField.field.getAnnotation(Node.class);
                if (node == null) {
                    continue;
                }
                name = node.name();
                tag = node.tag();
                if (name.equals("")) {
                    name = subField.field.getName();
                }
                if (tag.equals("")) {
                    tag = name;
                }
            } else {
                name = subField.field.getName();
                tag = name;
            }

            Object mapSubType;
            if (typeHolder.isClass) {
                mapSubType = subField.field.getType();
            } else {
                try {
                    mapSubType = subField.field.get(type);
                    if(mapSubType == null) {
                        mapSubType = subField.field.getType();
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            INode subNode = nodeCreator.createSubNode(mapSubType, name, tag, false, new ObjectField(subField.field));

            Class<?> subClass = mapSubType instanceof Class<?> ? (Class<?>) mapSubType : mapSubType.getClass();
            boolean isSubMapToArray = isMapToArray(subClass);
            if (isSubMapToArray) {
                subNode.setMapToArray(true);
            }

            subNodeArray.add(subNode);

            if (fieldIsPrimitive && NodeUtils.isPrimitive(subClass)) {
                fieldIsPrimitive = false;
            }
        }

        size = subNodeArray.size();
        INode[] structureNodes = new INode[size];
        for (int i = 0; i < size; i++) {
            structureNodes[i] = subNodeArray.get(i);
        }

        if (fieldIsPrimitive || (typeHolder.isClass && isAnnotation(clazz))) {
            mSplitNodesMap.put(clazz, nodes);
        }

        return structureNodes;
    }

    private static boolean isAnnotation(Class<?> clazz) {
        return new AnnotationValue(clazz).annotation;
    }


    private static boolean isMapToArray(Class<?> clazz) {
        return new AnnotationValue(clazz).mapToArray;
    }

    private static boolean isIterationParent(Class<?> clazz) {
        return new AnnotationValue(clazz).iterationParent;
    }

    private static ArrayList<SubField> getFieldArray(Class<?> clazz) {
        ArrayList<SubField> fieldList = new ArrayList<>();
        while(clazz != Object.class) {
            boolean annotation = isAnnotation(clazz);
            Field[] fieldArray = clazz.getDeclaredFields();
            for (Field field : fieldArray) {
                Node node = field.getAnnotation(Node.class);
                if (annotation && node == null) {
                    continue;
                }

                Class<?> fieldType = field.getType();
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers)) {
                    continue;
                }
                if (Collection.class.isAssignableFrom(fieldType)) {
                    continue;
                }
                field.setAccessible(true);
                fieldList.add(new SubField(field, node));
            }

            while (!isIterationParent(clazz)) {
                clazz = clazz.getSuperclass();
            }
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }

    private static class SubField {
        private Field field;
        private Node node;

        private SubField(Field field, Node node) {
            this.field = field;
            this.node = node;
        }
    }

    private static class AnnotationValue {
        private final boolean annotation;
        private final boolean mapToArray;
        private final boolean iterationParent;

        private AnnotationValue(Class<?> clazz) {
            Nodes nodes = clazz.getAnnotation(Nodes.class);
            if (nodes == null) {
                annotation = IObject.class.isAssignableFrom(clazz);
                mapToArray = false;
                iterationParent = true;
            } else {
                annotation = nodes.annotation();
                mapToArray = nodes.mapToArray();
                iterationParent = nodes.iterationParent();
            }
        }
    }
}
