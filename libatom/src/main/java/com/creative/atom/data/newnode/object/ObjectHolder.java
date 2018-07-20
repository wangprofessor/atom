package com.creative.atom.data.newnode.object;

import com.creative.atom.data.newnode.BaseHolder;

public class ObjectHolder extends BaseHolder {
    public ObjectHolder() {
        super(new ObjectCreator(), new ObjectSplitter());
    }
}
