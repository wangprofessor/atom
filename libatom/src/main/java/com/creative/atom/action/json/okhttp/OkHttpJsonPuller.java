package com.creative.atom.action.json.okhttp;

import com.creative.atom.action.SimpleLogger;
import com.creative.atom.action.common.CallbackException;
import com.creative.atom.action.common.IPull;
import com.creative.atom.action.json.IJsonPuller;
import com.creative.atom.action.json.JsonRequest;
import com.creative.atom.action.json.JsonResponse;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

/**
 * Created by wangshouchao on 2017/12/29.
 */

public class OkHttpJsonPuller implements IJsonPuller {
    private static final String TAG = "OkHttpJsonPuller";

    private final JsonHttpClient mJsonHttpClient = new JsonHttpClient();

    @Override
    public void open() {
        mJsonHttpClient.open();
    }

    @Override
    public void pull(JsonRequest jsonRequest, IPull<JsonResponse> callback) {
        mJsonHttpClient.enqueue(new JsonHttpClient.IJsonHttpClientCallback() {
            @Override
            public Request createRequest() {
                return OkHttpJsonPuller.createRequest(jsonRequest);
            }

            @Override
            public void onResponse(Response response) {
                parseResponse(response, callback,jsonRequest);
            }

            @Override
            public void onFailure(CallbackException e) {
                callback.onFailure(e);
            }

            @Override
            public void onTooMuch() {
                callback.onTooMuch();
            }
        });
    }

    public void setLogger(SimpleLogger.ILogger logger){
        if(logger != null) {
            SimpleLogger.setLogger(logger);
        }
    }


    @Override
    public void close() {
        mJsonHttpClient.close();
    }

    private static Request createRequest(JsonRequest jsonRequest) {
        String url = jsonRequest.url;
        JSONObject jsonObject = jsonRequest.jsonCreator.createJson();
        System.out.println(TAG + ", json:" + jsonObject);
        boolean doGzip = jsonRequest.doGzip;

        SimpleLogger.defaultLogger().i(TAG,url);

        Request.Builder builder = new Request.Builder();

        HashMap<String, String> header = jsonRequest.header;
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value != null) {
                    builder.header(key, value);
                }
            }
        }

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        if (doGzip) {
            builder.header("Content-Encoding", "gzip");
            body = gzip(body);
        }

        return builder
                .url(url)
                .post(body)
                .build();
    }

    private static RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() {
                return -1;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }

    private static void parseResponse(Response response, IPull<JsonResponse> callback, JsonRequest jsonRequest) {
        ResponseBody responseBody = response.body();
        if(responseBody == null) {
            callback.onFailure(new CallbackException("responseBody null", "http response"));
            return ;
        }

        String jsonString;
        try {
            jsonString = responseBody.string();
        } catch (IOException e) {
            callback.onFailure(new CallbackException("json error", "http response"));
            return;
        }

        if(jsonString == null || "".equals(jsonString)) {
            callback.onFailure(new CallbackException("jsonString null, response:" + response.toString(), "http response"));
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            callback.onGet(new JsonResponse(jsonObject), IPull.SOURCE_NET);

            String result = "\n--------------------"
                    + "\nurl: "     + response.request().url().toString()
                    + "\ndata: "    + jsonRequest.jsonCreator.createJson()
                    + "\nresult: "  + jsonString
                    + "\n--------------------";

            SimpleLogger.defaultLogger().d(TAG, result);
        } catch (JSONException e) {
            CallbackException callbackException = new CallbackException(jsonString, "http response");
            callback.onFailure(callbackException);
        }
    }
}
