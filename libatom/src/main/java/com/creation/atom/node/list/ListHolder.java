package com.creation.atom.node.list;

import com.creation.atom.node.BaseHolder;

public class ListHolder extends BaseHolder {
    public ListHolder() {
        super(new ListCreator(), new ListSplitter());
    }
}
