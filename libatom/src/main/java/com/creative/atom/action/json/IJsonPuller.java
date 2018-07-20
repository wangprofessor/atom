package com.creative.atom.action.json;

import com.creative.atom.action.common.IPull;

/**
 * Created by wangshouchao on 2017/12/29.
 */

public interface IJsonPuller {
    void open();
    void pull(JsonRequest jsonRequest, IPull<JsonResponse> callback);
    void close();
}
