package com.creative.atom.data.node;

public class BaseNodeCreator implements INodeCreator {
    protected static final int NODES_TYPE_UNDEFINE = 0;
    protected static final int NODES_TYPE_ARRAY = 1;
    protected static final int NODES_TYPE_MAP = 2;

    protected Class<?> nodeHolderClass;

    @Override
    public INodes createRootNodes(Object type) {
        return (INodes) createSubNode(type, null, null, false, null);
    }

    @Override
    public INode createSubNode(Object type, String name, String tag, boolean isArraySub, IField field) {
        if (type == null) {
            System.out.println("BaseNodeCreator, createSubNode, type:null");
            return createNode(name, isArraySub, false, null, false, tag, field);
        }

        INode subNode;
        TypeHolder typeHolder = NodeUtils.getTypeHolder(type);
        boolean isPrimitive = NodeUtils.isPrimitive(typeHolder.clazz);
        INodeHolder nodeHolder = NodesCreator.getNodeHolderByType(type);

        if (isPrimitive || !isMatch(nodeHolder)) {
            subNode = createNode(name, isArraySub, typeHolder.isClass, type, !isPrimitive, tag, field);
        } else {
            int nodeType = getNodeType(typeHolder.clazz);
            if (nodeType == NODES_TYPE_ARRAY) {
                int length;
                if (typeHolder.isClass) {
                    length = 1;
                } else {
                    length = getArrayLength(type);
                }
                subNode = createNodeArray(name, isArraySub, typeHolder.isClass, type, tag, field, length);
            } else if (nodeType == NODES_TYPE_MAP) {
                subNode = createNodeMap(name, isArraySub, typeHolder.isClass, type, tag, field);
            } else {
                throw new RuntimeException();
            }
        }
        return subNode;
    }

    @Override
    public void setNodeHolderClass(Class<?> clazz) {
        nodeHolderClass = clazz;
    }

    protected int getNodeType(Class<?> clazz) {
        int nodeType;
        if (clazz.isArray()) {
            nodeType = NODES_TYPE_ARRAY;
        } else {
            nodeType = NODES_TYPE_MAP;
        }
        return nodeType;
    }

    private boolean isMatch(INodeHolder nodeHolder) {
        return nodeHolder.getClass() == nodeHolderClass;
    }

    protected int getArrayLength(Object type) {
        return 0;
    }

    protected INode createNode(String name, boolean isArraySub, boolean isClass, Object type, boolean isVariation, String tag, IField field) {
        return new BaseNode(name, isArraySub, isClass, type, isVariation);
    }

    protected INode createNodeArray(String name, boolean isArraySub, boolean isClass, Object type, String tag, IField field, int length) {
        return new BaseNodeArray(name, type, isArraySub, isClass, false, length);
    }

    protected INode createNodeMap(String name, boolean isArraySub, boolean isClass, Object type, String tag, IField field) {
        return new BaseNodeMap(name, type, isArraySub, isClass, true);
    }
}
