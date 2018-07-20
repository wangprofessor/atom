package com.creative.atom.node.map;

import com.creative.atom.node.BaseHolder;

public class MapHolder extends BaseHolder {
    public MapHolder() {
        super(new MapCreator(), new MapSplitter());
    }
}
