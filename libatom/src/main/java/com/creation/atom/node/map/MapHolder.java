package com.creation.atom.node.map;

import com.creation.atom.node.BaseHolder;

public class MapHolder extends BaseHolder {
    public MapHolder() {
        super(new MapCreator(), new MapSplitter());
    }
}
