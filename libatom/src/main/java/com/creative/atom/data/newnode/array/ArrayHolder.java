package com.creative.atom.data.newnode.array;

import com.creative.atom.data.newnode.BaseHolder;

public class ArrayHolder extends BaseHolder {
    public ArrayHolder() {
        super(new ArrayCreator(), new ArraySplitter());
    }
}
