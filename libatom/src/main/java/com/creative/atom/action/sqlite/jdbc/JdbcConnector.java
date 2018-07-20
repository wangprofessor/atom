package com.creative.atom.action.sqlite.jdbc;

import com.creative.atom.action.sqlite.IConnector;
import com.creative.atom.action.sqlite.IDb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by wangshouchao on 2017/12/28.
 */

class JdbcConnector implements IConnector {
    private Connection mConnection;

    @Override
    public void connect(Object context, String dbName, int version) {
        try {
            Class.forName("org.sqlite.JDBC");
            String path = System.getProperty("user.dir") + "/testDB/";
            String url = "jdbc:sqlite:" + path + dbName + ".db";
            mConnection = DriverManager.getConnection(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IDb openDatabase() {
        try {
            Statement statement = mConnection.createStatement();
            return new JdbcDb(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disconnect() {
        try {
            mConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
