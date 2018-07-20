package com.creative.atom.node.object;

import com.creative.atom.node.BaseHolder;

public class ObjectHolder extends BaseHolder {
    public ObjectHolder() {
        super(new ObjectCreator(), new ObjectSplitter());
    }
}
