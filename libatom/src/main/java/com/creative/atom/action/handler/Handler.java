package com.creative.atom.action.handler;

/**
 * Created by wangshouchao on 2018/1/26.
 */

public class Handler {
    private MessageQueue messageQueue;

    public Handler() {
        Looper looper = Looper.myLooper();
        if (looper == null) {
            throw new RuntimeException("Can't create handler inside thread that has not called Looper.prepare()");
        }
        messageQueue = looper.messageQueue;
    }

    public void post(Runnable runnable) {
        sendMessage(new RunnableMessage(runnable));
    }

    public void sendMessage(Message msg) {
        msg.target = this;
        messageQueue.enqueueMessage(msg);
    }

    public void handleMessage(Message msg) {

    }
}
