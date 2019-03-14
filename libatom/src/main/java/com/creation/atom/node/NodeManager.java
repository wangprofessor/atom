package com.creation.atom.node;

public class NodeManager {
    public static INode create(Object origin) {
        Holders holders = new Holders();

        Type type = Type.create(origin);
        IHolder holder = holders.getHolder(type.clazz);

        if (holder == null) {
            return HolderManager.sPrimitiveHolder.getCreator().createNode(origin);
        }

        ICreator creator = holder.getCreator();
        INode rootNode = creator.createNode(origin);

        createInner(holders, holder, rootNode);
        return rootNode;
    }

    private static void createInner(Holders holders, IHolder holder, INode node) {
        ISplitter splitter = holder.getSplitter();

        IParent parent = splitter.split(node);
        node.setParent(parent);

        INode[] childNodeArray = parent.getChildren();
        for (INode childNode : childNodeArray) {
            if (!childNode.isChild()) {
                throw new RuntimeException();
            }

            Type childType = Type.create(childNode);
            IHolder childHolder = holders.getHolder(childType.clazz);
            if (childHolder != null) {
                createInner(holders, childHolder, childNode);
            }
        }
    }
}
