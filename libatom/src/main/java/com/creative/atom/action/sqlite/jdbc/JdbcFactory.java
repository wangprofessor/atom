package com.creative.atom.action.sqlite.jdbc;

import com.creative.atom.action.sqlite.IConnector;
import com.creative.atom.action.sqlite.IConnectorFactory;

/**
 * Created by wangshouchao on 2017/12/28.
 */

public class JdbcFactory implements IConnectorFactory {
    @Override
    public IConnector create() {
        return new JdbcConnector();
    }
}
