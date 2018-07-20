package com.creative.atom.action.common;

/**
 * Created by wangshouchao on 2017/10/13.
 */
public class PullAdapter<T> implements IPull<T> {

    @Override
    public void onFailure(CallbackException e) {

    }

    @Override
    public void onTooMuch() {

    }

    @Override
    public void onGet(T data, int getType) {

    }
}
