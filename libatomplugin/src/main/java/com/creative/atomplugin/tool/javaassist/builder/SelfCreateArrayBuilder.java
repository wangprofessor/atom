package com.creative.atomplugin.tool.javaassist.builder;

import com.creative.atom.data.node.INodeMap;
import com.creative.atomplugin.tool.javaassist.IMethodBuilder;

public class SelfCreateArrayBuilder implements IMethodBuilder {
    private static final String selfCreateArrayM = "selfCreateArray";
    private static final String lengthV = "length";

    @Override
    public String buildMethod(INodeMap nodeMap) {
        int depth = 0;
        String result = BuilderUtils.println(depth, String.format("public Object %s(int %s) {", selfCreateArrayM, lengthV));
        result += buildBody(nodeMap);
        result += BuilderUtils.println(depth,"}");
        return result;
    }

    @Override
    public String buildBody(INodeMap nodeMap) {
        Class<?> clazz = nodeMap.getClazz();

        int depth = 1;

        String buildString = BuilderUtils.printLogo(clazz, selfCreateArrayM, depth);
        buildString += BuilderUtils.println(depth, String.format("return new %s[%s];", clazz.getName(), lengthV));

        return buildString;
    }
}
