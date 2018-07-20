package com.creative.atom.action.sqlite.jdbc;

import com.creative.atom.action.sqlite.ICursor;
import com.creative.atom.action.sqlite.IDb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by chuck on 2017/12/6.
 */

class JdbcDb implements IDb {
    private Statement mStatement;

    JdbcDb(Statement statement) {
        mStatement = statement;
    }

    @Override
    public void execSql(String sql) {
        try {
            System.out.println(sql);
            mStatement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ICursor query(String sql) {
        try {
            System.out.println(sql);
            ResultSet resultSet = mStatement.executeQuery(sql);
            return new JdbcCursor(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            mStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
