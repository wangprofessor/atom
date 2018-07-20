package com.creative.atom.data.newnode.primitive;

import com.creative.atom.data.newnode.BaseHolder;
import com.creative.atom.data.newnode.ISplitter;

public class PrimitiveHolder extends BaseHolder {
    public PrimitiveHolder() {
        super(new PrimitiveCreator(), null);
    }

    @Override
    public ISplitter getSplitter() {
        throw new RuntimeException();
    }
}
