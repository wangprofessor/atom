package com.creative.atomplugin.tool.javaassist.builder;

import com.creative.atom.data.node.INode;
import com.creative.atom.data.node.INodeCreator;
import com.creative.atom.data.node.INodeMap;
import com.creative.atom.data.node.NodeUtils;
import com.creative.atom.data.node.IField;
import com.creative.atom.data.node.object.IObjectNode;
import com.creative.atom.data.node.object.ObjectNodeHolder;
import com.creative.atomplugin.tool.javaassist.IMethodBuilder;

import java.util.Map;

public class SelfSplitBuilder implements IMethodBuilder {
    private static final String INodeC = INode.class.getName();
    private static final String ObjectNodeHolderC = ObjectNodeHolder.class.getName();
    private static final String INodeCreatorC = INodeCreator.class.getName();

    private static final String selfSplitSubsM = "selfSplitSubs";
    private static final String getNodeCreatorM = "getNodeCreator";
    private static final String createSubNodeM = "createSubNode";

    private static final String nodeArrayV = "nodeArray";
    private static final String nodeCreatorV = "nodeCreator";
    private static final String typeV = "type";

    @Override
    public String buildMethod(INodeMap nodeMap) {
        int depth = 0;
        String buildString = BuilderUtils.println(depth, String.format("public %s[] %s() {", INodeC, selfSplitSubsM));
        buildString += buildBody(nodeMap);
        buildString += BuilderUtils.println(depth, "}");
        return buildString;
    }

    @Override
    public String buildBody(INodeMap nodeMap) {
        int depth = 1;

        String buildString = BuilderUtils.printLogo(nodeMap.getClazz(), selfSplitSubsM, depth);

        int size = nodeMap.getSubNodeMap().size();
        buildString += BuilderUtils.println(depth, String.format("%s[] %s = new %s[%s];", INodeC, nodeArrayV, INodeC, size));
        buildString += BuilderUtils.println(depth, String.format("%s %s = new %s().%s();", INodeCreatorC, nodeCreatorV, ObjectNodeHolderC, getNodeCreatorM));
        buildString += BuilderUtils.println(depth, String.format("Object %s;", typeV));
        buildString += buildBodyInner(depth, nodeMap);
        buildString += BuilderUtils.println(depth, String.format("return %s;", nodeArrayV));

        return buildString;
    }

    private String buildBodyInner(int depth, INodeMap nodeMap) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, INode> entry : nodeMap.getSubNodeMap().entrySet()) {
            IObjectNode objectNode = (IObjectNode) entry.getValue();
            IField field = objectNode.getField();
            Class<?> clazz = field.getType();
            String name = objectNode.name();
            String fieldName = field.getName();

            BuilderUtils.println(builder, depth, writeType(clazz, fieldName));
            String node = createSubNode(name, name, false);
            BuilderUtils.println(builder, depth, putIntoArray(i, node));

            i++;
        }
        return builder.toString();
    }

    private String putIntoArray(int i, String node) {
        return String.format("%s[%s] = %s;", nodeArrayV, i, node);
    }

    private String writeType(Class<?> clazz, String fieldName) {
        if (clazz.isPrimitive()) {
            return String.format("%s = %s;", typeV, BuilderUtils.primitiveToObject(clazz, "this." + fieldName));
        }
        return String.format("if (this.%s == null) { %s = %s.class; } else { %s = this.%s; }", fieldName, typeV, getClassString(clazz), typeV, fieldName);
    }

    private String createSubNode(String name, String tag, boolean isArraySub) {
        String nameString = name == null ? "%s" : "\"%s\"";
        String tagString = tag == null ? "%s" : "\"%s\"";
        return String.format("%s.%s(%s, " + nameString + ", " + tagString + ", %s, null)", nodeCreatorV, createSubNodeM, typeV, name, tag, isArraySub);
    }

    private String getClass(Class<?> clazz, String fieldName) {
        if (NodeUtils.isPrimitive(clazz)) {
            return clazz.getName() + ".class";
        } else if (clazz.isArray()) {
            return getClassString(clazz) + ".class";
        }
        return String.format("this.%s == null ? %s.class : this.%s.getClass()", fieldName, getClassString(clazz), fieldName);
    }

    private String getClassString(Class<?> clazz) {
        String classString;
        if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            classString = componentType.getName() + "[]";
        } else {
            classString = clazz.getName();
        }
        return classString;
    }
}
