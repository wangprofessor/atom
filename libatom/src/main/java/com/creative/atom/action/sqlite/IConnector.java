package com.creative.atom.action.sqlite;

/**
 * Created by wangshouchao on 2017/12/28.
 */

public interface IConnector {
    void connect(Object context, String dbName, int version);
    IDb openDatabase();
    void disconnect();
}
