package com.creative.atom.action.handler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wangshouchao on 2018/1/26.
 */

class MessageQueue {
    private BlockingQueue<Message> mBlockingQueue = new LinkedBlockingQueue<>();

    Message next() {
        try {
            return mBlockingQueue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    void enqueueMessage(Message message) {
        try {
            mBlockingQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
