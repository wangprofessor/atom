package com.creative.atom.data.node.json;

import com.creative.atom.data.node.IValueCreator;

import org.json.JSONArray;
import org.json.JSONObject;

class JsonValueCreator implements IValueCreator {
    @Override
    public Object createArrayValue(Class<?> targetClass, Class<?> sourceClass, int length) {
        return new JSONArray();
    }

    @Override
    public Object createMapValue(Class<?> targetClass, Class<?> sourceClass) {
        return new JSONObject();
    }
}
