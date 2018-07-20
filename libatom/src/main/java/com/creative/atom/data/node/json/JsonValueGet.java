package com.creative.atom.data.node.json;

import com.creative.atom.data.node.INode;
import com.creative.atom.data.node.IValueGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangshouchao on 2017/12/15.
 */

class JsonValueGet implements IValueGet {
    @Override
    public Object getMapValue(INode node, Object parent, String name) {
        JSONObject jsonObject = (JSONObject) parent;
        Object value = null;
        try {
            value = jsonObject.get(name);
        } catch (JSONException ignored) {}
        return value;
    }

    @Override
    public Object getArrayValue(INode node, Object parent, int index) {
        JSONArray jsonArray = (JSONArray) parent;
        Object value = null;
        try {
            value = jsonArray.get(index);
        } catch (JSONException ignored) {}
        return value;
    }
}
