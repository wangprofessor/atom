package com.creative.atomplugin.tool.javaassist.builder;

import com.creative.atom.data.node.INode;
import com.creative.atom.data.node.INodeMap;
import com.creative.atom.data.node.IField;
import com.creative.atom.data.node.object.IObjectNode;
import com.creative.atomplugin.tool.javaassist.IMethodBuilder;

import java.util.Map;

public class SelfGetSubBuilder implements IMethodBuilder {
    private static final String valueV = "value";
    private static final String nameV = "name";
    private static final String selfGetSubM = "selfGetSub";

    @Override
    public String buildMethod(INodeMap nodeMap) {
        int depth = 0;
        String buildString = BuilderUtils.println(depth, String.format("public Object %s(String %s) {", selfGetSubM, nameV));
        buildString += buildBody(nodeMap);
        buildString += BuilderUtils.println(depth, "}");
        return buildString;
    }

    @Override
    public String buildBody(INodeMap nodeMap) {
        int depth = 1;

        String buildString = BuilderUtils.printLogo(nodeMap.getClazz(), selfGetSubM, depth);

        buildString += BuilderUtils.println(depth, String.format("Object %s = null;", valueV));
        buildString += buildBodyInner(depth, nodeMap);
        buildString += BuilderUtils.println(depth, String.format("return %s;", valueV));

        return buildString;
    }

    private String buildBodyInner(int depth, INodeMap nodeMap) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, INode> entry : nodeMap.getSubNodeMap().entrySet()) {
            IObjectNode objectNode = (IObjectNode) entry.getValue();
            IField field = objectNode.getField();
            String fieldName = field.getName();
            String name = entry.getKey();
            Class<?> clazz = field.getType();
            BuilderUtils.println(builder, depth, String.format("if (%s.equals(\"%s\")) {", nameV, name));

            depth++;
            BuilderUtils.println(builder, depth, String.format("%s = %s;", valueV, BuilderUtils.primitiveToObject(clazz, "this." + fieldName)));
            depth--;

            BuilderUtils.println(builder, depth, "}");
        }
        return builder.toString();
    }
}
