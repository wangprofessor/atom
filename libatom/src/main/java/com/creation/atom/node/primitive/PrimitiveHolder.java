package com.creation.atom.node.primitive;

import com.creation.atom.node.BaseHolder;
import com.creation.atom.node.ISplitter;

public class PrimitiveHolder extends BaseHolder {
    public PrimitiveHolder() {
        super(new PrimitiveCreator(), null);
    }

    @Override
    public ISplitter getSplitter() {
        throw new RuntimeException();
    }
}
