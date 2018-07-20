package com.creative.atomplugin.tool.javaassist.builder;

import com.creative.atom.data.node.INodeMap;
import com.creative.atomplugin.tool.javaassist.IMethodBuilder;

public class SelfGetArraySubBuilder implements IMethodBuilder {
    private static final String selfGetArraySubM = "selfGetArraySub";
    private static final String arrayV = "array";
    private static final String indexV = "index";

    @Override
    public String buildMethod(INodeMap nodeMap) {
        int depth = 0;
        String buildString = BuilderUtils.println(depth, String.format("public Object %s(Object %s, int %s) {", selfGetArraySubM, arrayV, indexV));
        buildString += buildBody(nodeMap);
        buildString += BuilderUtils.println(depth, "}");
        return buildString;
    }

    @Override
    public String buildBody(INodeMap nodeMap) {
        int depth = 1;

        String buildString = BuilderUtils.printLogo(nodeMap.getClazz(), selfGetArraySubM, depth);
        buildString += buildBodyInner(depth, nodeMap);

        return buildString;
    }

    private String buildBodyInner(int depth, INodeMap nodeMap) {
        StringBuilder builder = new StringBuilder();
        Class<?> clazz = nodeMap.getClazz();
        BuilderUtils.println(builder, depth, String.format("return ((%s[]) %s)[%s];", clazz.getName(), arrayV, indexV));
        return builder.toString();
    }
}
