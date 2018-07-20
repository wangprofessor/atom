package com.creative.atom.action.handler;

/**
 * Created by wangshouchao on 2018/1/26.
 */

public class Message {
    Handler target;
    public Object obj;
    public int what;

    @Override
    public String toString() {
        return "what=" + what + " obj=" + obj.toString();
    }
}
