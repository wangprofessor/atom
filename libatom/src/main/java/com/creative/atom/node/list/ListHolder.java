package com.creative.atom.node.list;

import com.creative.atom.node.BaseHolder;

public class ListHolder extends BaseHolder {
    public ListHolder() {
        super(new ListCreator(), new ListSplitter());
    }
}
