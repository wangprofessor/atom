package com.creative.atom.action.json;

import com.creative.atom.action.common.IPull;

/**
 * Created by wangshouchao on 2017/12/29.
 */

public class JsonPuller implements IJsonPuller {
    private static IJsonPuller sJsonPuller;

    public static void init(IJsonPuller jsonPuller) {
        sJsonPuller = jsonPuller;
    }

    @Override
    public void open() {
        checkInit();
        sJsonPuller.open();
    }

    @Override
    public void pull(JsonRequest jsonRequest, IPull<JsonResponse> callback) {
        checkInit();
        sJsonPuller.pull(jsonRequest, callback);
    }

    @Override
    public void close() {
        checkInit();
        sJsonPuller.close();
    }

    private void checkInit() {
        if (sJsonPuller == null) {
            throw new RuntimeException("not init");
        }
    }
}
