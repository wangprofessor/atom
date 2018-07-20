package com.creative.atom.action.common;

/**
 * Created by wangshouchao on 2017/10/13.
 */
public class PullListAdapter <T> implements IPullList<T> {

    @Override
    public void onFailure(CallbackException e) {

    }

    @Override
    public void onTooMuch() {

    }

    @Override
    public void onGetList(PullDataList<T> dataList) {

    }
}
