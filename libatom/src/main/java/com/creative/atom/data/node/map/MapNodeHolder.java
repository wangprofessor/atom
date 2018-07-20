package com.creative.atom.data.node.map;

import com.creative.atom.data.node.BaseNodeHolder;
import com.creative.atom.data.node.INodeHolder;

/**
 * Created by wangshouchao on 2017/12/15.
 */

public class MapNodeHolder extends BaseNodeHolder {
    public MapNodeHolder() {
        super(
                new MapNodeCreator(),
                new MapValueCreator(),
                new MapNodeSplit(),
                new MapValueSet(),
                new MapValueGet()
        );
    }

    @Override
    public INodeHolder copy() {
        return new MapNodeHolder();
    }
}
