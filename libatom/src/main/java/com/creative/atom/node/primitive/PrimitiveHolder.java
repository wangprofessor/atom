package com.creative.atom.node.primitive;

import com.creative.atom.node.BaseHolder;
import com.creative.atom.node.ISplitter;

public class PrimitiveHolder extends BaseHolder {
    public PrimitiveHolder() {
        super(new PrimitiveCreator(), null);
    }

    @Override
    public ISplitter getSplitter() {
        throw new RuntimeException();
    }
}
