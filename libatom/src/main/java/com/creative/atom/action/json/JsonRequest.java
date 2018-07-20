package com.creative.atom.action.json;

import java.util.HashMap;

/**
 * Created by wangshouchao on 2017/12/29.
 */

public class JsonRequest {
    public final String url;
    public final IJsonCreator jsonCreator;
    public final boolean doGzip;
    public final HashMap<String, String> header;

    public JsonRequest(String url, IJsonCreator jsonCreator, HashMap<String, String> header) {
        this(url, jsonCreator, header, false);
    }

    public JsonRequest(String url, IJsonCreator jsonCreator, HashMap<String, String> header, boolean doGzip) {
        this.url = url;
        this.jsonCreator = jsonCreator;
        this.header = header;
        this.doGzip = doGzip;
    }
}
