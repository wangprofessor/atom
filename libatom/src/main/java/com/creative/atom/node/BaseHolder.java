package com.creative.atom.node;

public class BaseHolder implements IHolder {
    private final ICreator creator;
    private final ISplitter splitter;

    protected BaseHolder(ICreator creator, ISplitter splitter) {
        this.creator = creator;
        this.splitter = splitter;

        if (splitter != null) {
            splitter.setCreator(creator);
        }
    }

    @Override
    public ICreator getCreator() {
        return creator;
    }

    @Override
    public ISplitter getSplitter() {
        return splitter;
    }
}
