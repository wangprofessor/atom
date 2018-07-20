package com.creative.atom.action.common;

/**
 * Created by wangshouchao on 2017/10/13.
 */
public interface IPull<T> extends ITooMuchFailure {
    int SOURCE_UNDEFINE = -1;
    int SOURCE_CACHE = 0;
    int SOURCE_DATABASE = 1;
    int SOURCE_NET = 2;

    void onGet(T data, int getType);
}
