package com.creative.atom.action.common;

/**
 * Created by wangshouchao on 2018/1/18.
 */

public interface ITransform <INPUT, OUTPUT> {
    OUTPUT transform(INPUT input);
}
