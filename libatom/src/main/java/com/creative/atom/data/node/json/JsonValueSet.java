package com.creative.atom.data.node.json;

import com.creative.atom.data.node.INode;
import com.creative.atom.data.node.IValueSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangshouchao on 2017/12/15.
 */

class JsonValueSet implements IValueSet {
    @Override
    public void setMapValue(INode targetNode, INode sourceNode, Object parent, String name, Object value) {
        JSONObject jsonObject = (JSONObject) parent;
        try {
            jsonObject.put(name, value);
        } catch (JSONException ignored) {}
    }

    @Override
    public void setArrayValue(INode targetNode, INode sourceNode, Object parent, int index, Object value) {
        JSONArray jsonArray = (JSONArray) parent;
        try {
            jsonArray.put(index, value);
        } catch (JSONException ignored) {}
    }
}
