package com.creative.atom.action.sqlite;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangshouchao on 2017/12/28.
 */

public class ConnectorManager {
    private final Object mContext;
    private final String mUid;
    private final int mVersion;
    private final HashMap<String, Connector> mOpenHelperMap = new HashMap<>();
    private final HashMap<String, IDb> mDbMap = new HashMap<>();

    public ConnectorManager(Object context, String uid, int version, IConnectorFactory connectorFactory) {
        mContext = context;
        mUid = uid;
        mVersion = version;
        Connector.initFactory(connectorFactory);
    }

    private Connector connect(String databaseName) {
        Connector connector = new Connector();
        connector.connect(mContext, databaseName, mVersion);
        mOpenHelperMap.put(databaseName, connector);
        return connector;
    }

    public IDb openDatabase(String dbName) {
        String databaseName = getDatabaseName(dbName);
        IDb db = mDbMap.get(databaseName);
        if (db == null) {
            Connector connector = mOpenHelperMap.get(databaseName);
            if (connector == null) {
                connector = connect(databaseName);
            }
            System.out.println("open database:" + dbName);
            db = connector.openDatabase();
            mDbMap.put(databaseName, db);
        }
        return db;
    }

    public void closeDatabase(String dbName) {
        String databaseName = getDatabaseName(dbName);
        IDb db = mDbMap.get(databaseName);
        if (db == null) {
            return;
        }
        System.out.println("close database:" + dbName);
        db.close();
        mDbMap.remove(databaseName);
    }

    public void disconnectAll() {
        for (Map.Entry<String, IDb> entry : mDbMap.entrySet()) {
            IDb db = entry.getValue();
            db.close();
        }
        mDbMap.clear();
        for (Map.Entry<String, Connector> entry : mOpenHelperMap.entrySet()) {
            Connector openHelper = entry.getValue();
            openHelper.disconnect();
        }
        mOpenHelperMap.clear();
    }

    public void disconnect(String dbName) {
        String databaseName = getDatabaseName(dbName);
        Connector openHelper = mOpenHelperMap.get(databaseName);
        if (openHelper != null) {
            openHelper.disconnect();
            mOpenHelperMap.remove(databaseName);
        }
        closeDatabase(dbName);
    }

    private String getDatabaseName(String databaseName) {
        return databaseName + "_" + mUid;
    }
}
