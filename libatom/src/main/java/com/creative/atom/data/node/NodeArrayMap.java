package com.creative.atom.data.node;

import java.util.ArrayList;

/**
 * Created by wangshouchao on 2017/12/17.
 */

public class NodeArrayMap <K, V> {
    private ArrayList<K> mKeyList = new ArrayList<>();
    private ArrayList<V> mValueList = new ArrayList<>();

    public void put(K key, V value) {
        int index = mKeyList.indexOf(key);
        if(index < 0) {
            mKeyList.add(key);
            mValueList.add(value);
        } else {
            mValueList.remove(index);
            mValueList.add(index, value);
        }
    }

    public V get(K key) {
        int index = mKeyList.indexOf(key);
        if(index < 0) {
            return null;
        }
        return mValueList.get(index);
    }
}
