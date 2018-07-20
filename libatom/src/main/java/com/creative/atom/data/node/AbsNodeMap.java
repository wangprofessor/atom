package com.creative.atom.data.node;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangshouchao on 2017/12/12.
 */

public class AbsNodeMap implements INodeMap {
    protected final INode mNode;

    private HashMap<String, INode> mNodeMap = new HashMap<>();
    private boolean mIsSplit = false;
    private final boolean mSupportAdd;

    public AbsNodeMap(INode node, boolean supportAdd) {
        mNode = node;
        mSupportAdd = supportAdd;
    }

    @Override
    public String name() {
        return mNode.name();
    }

    @Override
    public void setName(String name) {
        mNode.setName(name);
    }

    @Override
    public boolean isArraySub() {
        return mNode.isArraySub();
    }

    @Override
    public void setArraySub(boolean isArraySub) {
        mNode.setArraySub(isArraySub);
    }

    @Override
    public void setMapToArray(boolean isMapToArray) {
        mNode.setMapToArray(isMapToArray);
    }

    @Override
    public boolean isMapToArray() {
        return mNode.isMapToArray();
    }

    @Override
    public boolean isClass() {
        return mNode.isClass();
    }

    @Override
    public Class<?> getClazz() {
        return mNode.getClazz();
    }

    @Override
    public Object getType() {
        return mNode.getType();
    }

    @Override
    public boolean isVariation() {
        return mNode.isVariation();
    }

    @Override
    public void setVariation(boolean isVariation) {
        mNode.setVariation(isVariation);
    }

    @Override
    public void setVariationNodes(INodes nodes) {
        mNode.setVariationNodes(nodes);
    }

    @Override
    public INodes getVariationNodes() {
        return mNode.getVariationNodes();
    }

    @Override
    public void setIsSplit(boolean isSplit) {
        this.mIsSplit = isSplit;
    }

    @Override
    public boolean isSplit() {
        return mIsSplit;
    }

    @Override
    public INode[] getSplitSubNodes() {
        return NodeUtils.getSplitSubNodes(mNodeMap);
    }

    @Override
    public void copySubNodesFrom(INodes nodes) {
        if (!(nodes instanceof INodeMap)) {
            throw new RuntimeException();
        }
        mNodeMap = ((INodeMap) nodes).getSubNodeMap();
    }

    @Override
    public HashMap<String, INode> getSubNodeMap() {
        return mNodeMap;
    }

    @Override
    public void mapSetType(INode node) {
        mNodeMap.put(node.name(), node);
    }

    @Override
    public INode mapGetType(String name) {
        return mNodeMap.get(name);
    }

    @Override
    public void mapSetTypeMap(HashMap<String, INode> nodeMap) {
        mNodeMap = nodeMap;
    }

    @Override
    public HashMap<String, INode> mapGetTypeMap() {
        return mNodeMap;
    }

    @Override
    public boolean supportAdd() {
        return mSupportAdd;
    }
}
