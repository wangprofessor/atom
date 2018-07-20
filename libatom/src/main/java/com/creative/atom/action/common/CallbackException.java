package com.creative.atom.action.common;

/**
 * Created by wangshouchao on 2017/9/28.
 */

public class CallbackException extends Exception {
    //网络错误
    public static final int CODE_NET_ERR = -1;
    public long code;
    public String errorMsg;
    public String service;
    public long systemTime;
    public Object obj;

    public CallbackException() {
        this(null, null, null);
    }

    public CallbackException(String message, String service) {
        this(message, null, service);
    }

    public CallbackException(Throwable e, String service) {
        this(null, e, service);
    }

    public CallbackException(String message, Throwable e, String service) {
        super(message, e);
        this.service = service;
        systemTime = System.currentTimeMillis();
    }
}
