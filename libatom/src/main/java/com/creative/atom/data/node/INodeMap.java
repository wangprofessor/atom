package com.creative.atom.data.node;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangshouchao on 2017/12/12.
 */

public interface INodeMap extends INodes {
    HashMap<String, INode> getSubNodeMap();
    void mapSetType(INode node);
    INode mapGetType(String name);
    void mapSetTypeMap(HashMap<String, INode> nodeMap);
    HashMap<String, INode> mapGetTypeMap();
}
