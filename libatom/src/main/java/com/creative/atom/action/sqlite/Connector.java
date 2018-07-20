package com.creative.atom.action.sqlite;

/**
 * Created by wangshouchao on 2017/12/28.
 */

class Connector implements IConnector {
    private static IConnectorFactory sConnectorFactory;

    static void initFactory(IConnectorFactory connectorFactory) {
        sConnectorFactory = connectorFactory;
    }

    private IConnector mConnector;

    Connector() {
        mConnector = sConnectorFactory.create();
    }

    @Override
    public void connect(Object context, String dbName, int version) {
        mConnector.connect(context, dbName, version);
    }

    @Override
    public IDb openDatabase() {
        return mConnector.openDatabase();
    }

    @Override
    public void disconnect() {
        mConnector.disconnect();
    }
}
