package com.creative.atomplugin.tool.javaassist;

import com.creative.atom.data.node.INodeMap;

public interface IMethodBuilder {
    String buildMethod(INodeMap nodeMap);
    String buildBody(INodeMap nodeMap);
}
