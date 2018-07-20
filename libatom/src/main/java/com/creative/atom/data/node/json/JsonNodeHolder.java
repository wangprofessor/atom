package com.creative.atom.data.node.json;

import com.creative.atom.data.node.BaseNodeHolder;
import com.creative.atom.data.node.INodeHolder;

/**
 * Created by wangshouchao on 2017/12/15.
 */

public class JsonNodeHolder extends BaseNodeHolder {
    public JsonNodeHolder() {
        super(
                new JsonNodeCreator(),
                new JsonValueCreator(),
                new JsonNodeSplit(),
                new JsonValueSet(),
                new JsonValueGet()
        );
    }

    @Override
    public INodeHolder copy() {
        return new JsonNodeHolder();
    }
}
