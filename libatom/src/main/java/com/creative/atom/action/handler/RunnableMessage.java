package com.creative.atom.action.handler;

/**
 * Created by wangshouchao on 2018/1/26.
 */

class RunnableMessage extends Message {
    RunnableMessage(Runnable runnable) {
        obj = runnable;
    }
}
