package com.creation.atom.node.array;

import com.creation.atom.node.BaseHolder;

public class ArrayHolder extends BaseHolder {
    public ArrayHolder() {
        super(new ArrayCreator(), new ArraySplitter());
    }
}
