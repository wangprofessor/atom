package com.creative.atomplugin.tool.javaassist.builder;

import com.creative.atom.data.node.INode;
import com.creative.atom.data.node.INodeMap;
import com.creative.atom.data.node.IField;
import com.creative.atom.data.node.object.IObjectNode;
import com.creative.atomplugin.tool.javaassist.IMethodBuilder;

import java.util.Map;

public class SelfSetSubBuilder implements IMethodBuilder {
    private static final String selfSetSubM = "selfSetSub";
    private static final String valueV = "value";
    private static final String nameV = "name";

    @Override
    public String buildMethod(INodeMap nodeMap) {
        int depth = 0;
        String buildString = BuilderUtils.println(depth, String.format("public void %s(String %s, Object %s) {", selfSetSubM, nameV, valueV));
        buildString += buildBody(nodeMap);
        buildString += BuilderUtils.println(depth, "}");
        return buildString;
    }

    @Override
    public String buildBody(INodeMap nodeMap) {
        int depth = 1;

        String buildString = BuilderUtils.printLogo(nodeMap.getClazz(), selfSetSubM, depth);
        buildString += buildBodyInner(depth, nodeMap);

        return buildString;
    }

    private String buildBodyInner(int depth, INodeMap nodeMap) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, INode> entry : nodeMap.getSubNodeMap().entrySet()) {
            IObjectNode objectNode = (IObjectNode) entry.getValue();
            IField field = objectNode.getField();
            Class<?> clazz = field.getType();
            String fieldName = field.getName();
            String className = getArrayName(field.getType());
            String name = entry.getKey();

            BuilderUtils.println(builder, depth, String.format("if (%s.equals(\"%s\")) {", nameV, name));

            depth++;
            BuilderUtils.println(builder, depth, String.format("this.%s = (%s) %s;", fieldName, className, BuilderUtils.objectToPrimitive(clazz, valueV)));
            depth--;

            BuilderUtils.println(builder, depth, "}");
        }
        return builder.toString();
    }

    private String getArrayName(Class<?> clazz) {
        if (clazz.isArray()) {
            Class<?> type = clazz.getComponentType();
            return getArrayName(type) + "[]";
        } else {
            return clazz.getName();
        }
    }
}
