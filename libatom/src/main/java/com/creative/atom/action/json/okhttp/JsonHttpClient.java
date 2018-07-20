package com.creative.atom.action.json.okhttp;

import com.creative.atom.action.common.CallbackException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;


/**
 * Created by wangshouchao on 2017/9/1.
 */
 class JsonHttpClient {
    private static final String TAG = "IMHttpClient";

    public interface IJsonHttpClientCallback {
        Request createRequest();
        void onResponse(Response response);
        void onFailure(CallbackException e);
        void onTooMuch();
    }

    private OkHttpClient mOkHttpClient;
    private BlockingQueue<Runnable> mQueue;
    private BlockingQueue<CallData> mCacheQueue;
    private ThreadPoolExecutor mExecutorService;

    void open() {
        mQueue = new LinkedBlockingQueue<>();
        mCacheQueue = new LinkedBlockingQueue<>();

        mExecutorService = new ThreadPoolExecutor(
                4,
                16,
                32,
                TimeUnit.SECONDS,
                mQueue,
                Util.threadFactory("OkHttp Dispatcher", false),
                new ThreadPoolExecutor.DiscardPolicy()
        );

        mOkHttpClient = new OkHttpClient.Builder()
                .dispatcher(new Dispatcher(mExecutorService))
                .connectTimeout(16, TimeUnit.SECONDS)
                .readTimeout(32, TimeUnit.SECONDS)
                .writeTimeout(32, TimeUnit.SECONDS)
                .build();

        startTimer(new Runnable() {
            @Override
            public void run() {
                if (mCacheQueue.isEmpty()) {
                    return;
                }
                CallData callData = mCacheQueue.poll();
                enqueueCall(callData.call, callData.callback);
            }
        });
    }

    void close() {
        mExecutorService.shutdown();
    }

    void enqueue(final IJsonHttpClientCallback callback) {
        System.out.println(TAG + " queue size:" + mQueue.size() + " cache queue size:" + mQueue.size());
        if(mCacheQueue.size() + mQueue.size() > 1024 * 2) {
            callback.onTooMuch();
            return;
        }

        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Call call = mOkHttpClient.newCall(callback.createRequest());
                mCacheQueue.offer(new CallData(call, callback));
            }
        });
    }

    private void enqueueCall(Call call, final IJsonHttpClientCallback callback) {
        System.out.println(TAG + " enqueue call, request:" + call.request());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(TAG + " onFailure:" + e.toString());
                CallbackException callbackException = new CallbackException(e, "enqueue call");
                if(e instanceof UnknownHostException){
                        callbackException.code=CallbackException.CODE_NET_ERR;
                }
                callback.onFailure(callbackException);
            }

            @Override
            public void onResponse(Call call, Response response) {
                callback.onResponse(response);
            }
        });
    }

    private void startTimer(final Runnable runnable) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                runnable.run();
                try {
                    Thread.sleep(1000 / 64);
                } catch (InterruptedException ignore) {}

                if (!mExecutorService.isShutdown()) {
                    mExecutorService.execute(this);
                }
            }
        });
    }

    private static class CallData {
        private final Call call;
        private final IJsonHttpClientCallback callback;

        private CallData(Call call, IJsonHttpClientCallback callback) {
            this.call = call;
            this.callback = callback;
        }
    }
}
