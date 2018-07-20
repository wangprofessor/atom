package com.creative.atom.action.handler;

/**
 * Created by wangshouchao on 2018/1/26.
 */

public class Looper {
    private static final ThreadLocal<Looper> threadLocal = new ThreadLocal<>();

    final MessageQueue messageQueue;

    private Looper() {
        messageQueue = new MessageQueue();
    }

    public static void prepare() {
        if (threadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        threadLocal.set(new Looper());
    }

    public static void loop() {
        Looper looper = myLooper();
        if (looper == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        MessageQueue messageQueue = looper.messageQueue;

        for (; ; ) {
            Message message = messageQueue.next();
            if (message == null) {
                break;
            }
            if (message instanceof RunnableMessage) {
                Runnable runnable = (Runnable) message.obj;
                runnable.run();
            } else {
                message.target.handleMessage(message);
            }
        }
    }

    public static Looper myLooper() {
        return threadLocal.get();
    }
}
