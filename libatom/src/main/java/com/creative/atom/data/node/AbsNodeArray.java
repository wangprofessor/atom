package com.creative.atom.data.node;

/**
 * Created by wangshouchao on 2017/12/12.
 */

public class AbsNodeArray implements INodeArray {
    protected final INode mNode;

    private INode[] mNodeArray;
    private boolean mIsSplit = false;
    private final boolean mSupportAdd;

    public AbsNodeArray(INode node, boolean supportAdd, int length) {
        mNode = node;
        mNodeArray = new INode[length];
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
        return mNodeArray;
    }

    @Override
    public void copySubNodesFrom(INodes nodes) {
        if (!(nodes instanceof INodeArray)) {
            throw new RuntimeException();
        }
        mNodeArray = ((INodeArray) nodes).getSubNodeArray();
    }

    @Override
    public boolean supportAdd() {
        return mSupportAdd;
    }

    @Override
    public INode[] getSubNodeArray() {
        return mNodeArray;
    }

    @Override
    public void arraySetType(int index, INode node) {
        mNodeArray[index] = node;
    }

    @Override
    public INode arrayGetType(int index) {
        return mNodeArray[index];
    }

    @Override
    public void arraySetTypeArray(INode[] nodeArray) {
        this.mNodeArray = nodeArray;
    }

    @Override
    public INode[] arrayGetTypeArray() {
        return mNodeArray;
    }
}
