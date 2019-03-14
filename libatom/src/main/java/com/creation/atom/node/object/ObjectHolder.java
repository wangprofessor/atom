package com.creation.atom.node.object;

import com.creation.atom.node.BaseHolder;

public class ObjectHolder extends BaseHolder {
    public ObjectHolder() {
        super(new ObjectCreator(), new ObjectSplitter());
    }
}
