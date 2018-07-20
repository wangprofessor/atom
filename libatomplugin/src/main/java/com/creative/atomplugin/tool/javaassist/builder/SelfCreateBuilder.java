package com.creative.atomplugin.tool.javaassist.builder;

import com.creative.atom.data.node.INodeMap;
import com.creative.atomplugin.tool.javaassist.IMethodBuilder;

public class SelfCreateBuilder implements IMethodBuilder {
    private static final String selfCreateM = "selfCreate";

    @Override
    public String buildMethod(INodeMap nodeMap) {
        int depth = 0;
        String result = BuilderUtils.println(depth, String.format("public %s %s() {", BuilderUtils.MoleculeC, selfCreateM));
        result += buildBody(nodeMap);
        result += BuilderUtils.println(depth,"}");
        return result;
    }

    @Override
    public String buildBody(INodeMap nodeMap) {
        Class<?> clazz = nodeMap.getClazz();

        int depth = 1;

        String buildString = BuilderUtils.printLogo(clazz, selfCreateM, depth);
        buildString += BuilderUtils.println(depth, String.format("return new %s();", clazz.getName()));

        return buildString;
    }
}
