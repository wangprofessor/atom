package com.creative.atom.data.node;

import java.util.HashMap;

public abstract class BaseNodeHolder implements INodeHolder {
    private final INodeCreator mNodeCreator;
    private final IValueCreator mValueCreator;
    private final INodeSplit mNodeSplit;
    private final IValueSet mValueSet;
    private final IValueGet mValueGet;

    protected BaseNodeHolder(INodeCreator nodeCreator, IValueCreator valueCreator, INodeSplit nodeSplit, IValueSet valueSet, IValueGet valueGet) {
        this.mNodeCreator = nodeCreator;
        this.mValueCreator = new ValueCreatorWrapper(valueCreator);
        this.mNodeSplit = new NodeSplitWrapper(nodeSplit);
        this.mValueSet = new ValueSetWrapper(valueSet);
        this.mValueGet = new ValueGetWrapper(valueGet);

        nodeCreator.setNodeHolderClass(getClass());
    }

    @Override
    public INodeCreator getNodeCreator() {
        return mNodeCreator;
    }

    @Override
    public IValueCreator getValueCreator() {
        return mValueCreator;
    }

    @Override
    public INodeSplit getNodeSplit() {
        return mNodeSplit;
    }

    @Override
    public IValueSet getValueSet() {
        return mValueSet;
    }

    @Override
    public IValueGet getValueGet() {
        return mValueGet;
    }

    private static final BaseValueCreator sBaseValueCreator = new BaseValueCreator();

    private static final HashMap<Class<?>, ISelfCreate> sSelfCreateMap = new HashMap<>();
    private static final HashMap<Class<?>, ISelfSub> sSelfSubMap = new HashMap<>();
    private static final HashMap<Class<?>, ISelfSplit> sSelfSplitMap = new HashMap<>();

    private static ISelfCreate getSelfCreate(Class<?> clazz) {
        ISelfCreate selfCreate = sSelfCreateMap.get(clazz);
        if (selfCreate == null) {
            selfCreate = (ISelfCreate) sBaseValueCreator.createMapValue(clazz, null);
            tryPutIntoMap(clazz, selfCreate);
        }
        return selfCreate;
    }

    private static ISelfSub getSelfSub(Class<?> clazz) {
        ISelfSub selfSub = sSelfSubMap.get(clazz);
        if (selfSub == null) {
            selfSub = (ISelfSub) sBaseValueCreator.createMapValue(clazz, null);
            tryPutIntoMap(clazz, selfSub);
        }
        return selfSub;
    }

    private static ISelfSplit getSelfSplit(Class<?> clazz) {
        ISelfSplit selfSplit = sSelfSplitMap.get(clazz);
        if (selfSplit == null) {
            selfSplit = (ISelfSplit) sBaseValueCreator.createMapValue(clazz, null);
            tryPutIntoMap(clazz, selfSplit);
        }
        return selfSplit;
    }

    private static void tryPutIntoMap(Class<?> clazz, Object self) {
        if (self instanceof ISelfCreate) {
            sSelfCreateMap.put(clazz, (ISelfCreate) self);
        }
        if (self instanceof ISelfSub) {
            sSelfSubMap.put(clazz, (ISelfSub) self);
        }
        if (self instanceof ISelfSplit) {
            sSelfSplitMap.put(clazz, (ISelfSplit) self);
        }
    }

    private static class ValueCreatorWrapper implements IValueCreator {
        private final IValueCreator valueCreator;

        private ValueCreatorWrapper(IValueCreator valueCreator) {
            this.valueCreator = valueCreator;
        }

        @Override
        public Object createArrayValue(Class<?> targetClass, Class<?> sourceClass, int length) {
            Class<?> clazz = sBaseValueCreator.getCreateArrayValueClass(targetClass, sourceClass);
            if (NodesCreator.APPLY_SELF && ISelfCreate.class.isAssignableFrom(clazz)) {
                ISelfCreate selfCreate = getSelfCreate(clazz);
                return selfCreate.selfCreateArray(length);
            } else {
                return valueCreator.createArrayValue(targetClass, sourceClass, length);
            }
        }

        @Override
        public Object createMapValue(Class<?> targetClass, Class<?> sourceClass) {
            Class<?> clazz = sBaseValueCreator.getCreateMapValueClass(targetClass, sourceClass);
            if (NodesCreator.APPLY_SELF && ISelfCreate.class.isAssignableFrom(clazz)) {
                ISelfCreate selfCreate = getSelfCreate(clazz);
                return selfCreate.selfCreate();
            } else {
                return valueCreator.createMapValue(targetClass, sourceClass);
            }
        }
    }

    private static class ValueSetWrapper implements IValueSet {
        private final IValueSet valueSet;

        private ValueSetWrapper(IValueSet valueSet) {
            this.valueSet = valueSet;
        }

        @Override
        public void setMapValue(INode targetNode, INode sourceNode, Object parent, String name, Object value) {
            if (NodesCreator.APPLY_SELF && parent instanceof ISelfSub) {
                ISelfSub selfSub = (ISelfSub) parent;
                selfSub.selfSetSub(name, value);
            } else {
                valueSet.setMapValue(targetNode, sourceNode, parent, name, value);
            }
        }

        @Override
        public void setArrayValue(INode targetNode, INode sourceNode, Object parent, int index, Object value) {
            Class<?> clazz = parent.getClass();
            Class<?> componentClass = clazz.getComponentType();
            if (NodesCreator.APPLY_SELF && clazz.isArray() && ISelfSub.class.isAssignableFrom(componentClass)) {
                ISelfSub selfSub = getSelfSub(componentClass);
                selfSub.selfSetArraySub(parent, index, value);
            } else {
                valueSet.setArrayValue(targetNode, sourceNode, parent, index, value);
            }
        }
    }

    private static class ValueGetWrapper implements IValueGet {
        private final IValueGet valueGet;

        private ValueGetWrapper(IValueGet valueGet) {
            this.valueGet = valueGet;
        }

        @Override
        public Object getMapValue(INode node, Object parent, String name) {
            if (NodesCreator.APPLY_SELF && parent instanceof ISelfSub) {
                ISelfSub selfSub = (ISelfSub) parent;
                return selfSub.selfGetSub(name);
            } else {
                return valueGet.getMapValue(node, parent, name);
            }
        }

        @Override
        public Object getArrayValue(INode node, Object parent, int index) {
            Class<?> clazz = parent.getClass();
            Class<?> componentClass = clazz.getComponentType();
            if (NodesCreator.APPLY_SELF && clazz.isArray() && ISelfSub.class.isAssignableFrom(componentClass)) {
                ISelfSub selfSub = getSelfSub(componentClass);
                return selfSub.selfGetArraySub(parent, index);
            } else {
                return valueGet.getArrayValue(node, parent, index);
            }
        }
    }

    private static class NodeSplitWrapper implements INodeSplit {
        private final INodeSplit nodeSplit;

        private NodeSplitWrapper(INodeSplit nodeSplit) {
            this.nodeSplit = nodeSplit;
        }

        @Override
        public INodes getCacheSplitNodes(Object type) {
            return nodeSplit.getCacheSplitNodes(type);
        }

        @Override
        public INode[] splitSubs(Object type, INodes nodes, INodeCreator nodeCreator, int splitType) {
            INode[] subs;
            TypeHolder typeHolder = NodeUtils.getTypeHolder(type);
            Class<?> clazz = typeHolder.clazz;
            if (NodesCreator.APPLY_SELF && ISelfSplit.class.isAssignableFrom(clazz)) {
                ISelfSplit selfSplit;
                if (typeHolder.isClass) {
                    selfSplit = getSelfSplit(clazz);
                } else {
                    selfSplit = (ISelfSplit) type;
                }
                subs = selfSplit.selfSplitSubs();
            } else {
                subs = nodeSplit.splitSubs(type, nodes, nodeCreator, splitType);
            }
            return subs;
        }
    }
}
