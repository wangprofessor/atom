package com.creative.atom.node.array;

import com.creative.atom.node.BaseHolder;

public class ArrayHolder extends BaseHolder {
    public ArrayHolder() {
        super(new ArrayCreator(), new ArraySplitter());
    }
}
