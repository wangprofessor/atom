package com.creative.atom.node.object;

import com.creative.atom.node.BaseSplitter;
import com.creative.atom.node.HolderManager;
import com.creative.atom.node.INode;
import com.creative.atom.node.IParent;
import com.creative.atom.node.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

class ObjectSplitter extends BaseSplitter {
    private static final HashMap<Class<?>, IParent> sClassCacheMap = new HashMap<>();
    private static final HashMap<Class<?>, IParent> sPrimitiveCacheMap = new HashMap<>();

    @Override
    public IParent split(INode node) {
        Type type = Type.create(node);

        IParent primitiveCache = sPrimitiveCacheMap.get(type.clazz);
        if (primitiveCache != null) {
            return primitiveCache;
        }

        Nodes nodes = type.clazz.getAnnotation(Nodes.class);
        if (nodes != null && type.isClass) {
            IParent classCache = sClassCacheMap.get(type.clazz);
            if (classCache != null) {
                return classCache;
            }
        }

        ArrayList<ChildField> childFieldList = getFieldArray(type.clazz);

        int size = childFieldList.size();
        ArrayList<INode> childNodeList = new ArrayList<>();
        boolean allFieldsPrimitive = true;
        for (int i = 0; i < size; i++) {
            ChildField childField = childFieldList.get(i);

            String name;
            String soleName;
            if (childField.nodes == null) {
                name = childField.field.getName();
                soleName = name;
            } else {
                name = childField.node.name();
                soleName = childField.node.soleName();
                if (name.equals("")) {
                    name = childField.field.getName();
                }
                if (soleName.equals("")) {
                    soleName = name;
                }
            }

            Object childOrigin;
            if (type.isClass) {
                childOrigin = childField.field.getType();
            } else {
                try {
                    childOrigin = childField.field.get(type.origin);
                    if(childOrigin == null) {
                        childOrigin = childField.field.getType();
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }



            ObjectChild child = (ObjectChild) creator.createChild(name);
            child.setField(childField.field);
            child.setName(name);
            child.setSoleName(soleName);

            INode childNode = creator.createNode(childOrigin);
            childNode.setChild(child);

            childNodeList.add(childNode);

            Type childType = Type.create(childOrigin);
            if (allFieldsPrimitive && HolderManager.isClassPrimitive(childType.clazz)) {
                allFieldsPrimitive = false;
            }
        }

        size = childNodeList.size();
        INode[] childNodeArray = new INode[size];
        for (int i = 0; i < size; i++) {
            childNodeArray[i] = childNodeList.get(i);
        }

        ObjectParent parent = (ObjectParent) creator.createParent(childNodeArray);
        if (nodes != null) {
            String name = nodes.name();
            String soleName = nodes.soleName();
            if (soleName.equals("")) {
                soleName = name;
            }
            parent.setName(name);
            parent.setSoleName(soleName);
        }

        if (nodes != null) {
            if (allFieldsPrimitive) {
                sPrimitiveCacheMap.put(type.clazz, parent);
            }
            if (type.isClass) {
                sClassCacheMap.put(type.clazz, parent);
            }
        }

        return parent;
    }

    private static ArrayList<ChildField> getFieldArray(Class<?> clazz) {
        HashMap<Class<?>, Nodes> classMap = new HashMap<>();
        HashSet<Class<?>> exceptClassSet = new HashSet<>();
        while(clazz != Object.class) {
            Nodes nodes = clazz.getAnnotation(Nodes.class);
            classMap.put(clazz, nodes);
            if (nodes == null) {
                continue;
            }
            Class<?>[] exceptArray = nodes.exceptParents();
            exceptClassSet.addAll(Arrays.asList(exceptArray));
            clazz = clazz.getSuperclass();
        }
        for (Class<?> exceptClass : exceptClassSet) {
            classMap.remove(exceptClass);
        }

        ArrayList<ChildField> childFieldList = new ArrayList<>();
        for (Map.Entry<Class<?>, Nodes> entry : classMap.entrySet()) {
            Class<?> eachClass = entry.getKey();
            Nodes nodes = entry.getValue();

            Field[] fieldArray = eachClass.getDeclaredFields();
            for (Field field : fieldArray) {
                Node node = field.getAnnotation(Node.class);
                if (nodes != null && node == null) {
                    continue;
                }

                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers)) {
                    continue;
                }
                field.setAccessible(true);
                childFieldList.add(new ChildField(field, nodes, node));
            }
        }

        return childFieldList;
    }

    private static class ChildField {
        private final Field field;
        private final Nodes nodes;
        private final Node node;

        private ChildField(Field field, Nodes nodes, Node node) {
            this.field = field;
            this.nodes = nodes;
            this.node = node;
        }
    }
}
