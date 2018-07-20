package com.creative.atom.action.sqlite;

/**
 * Created by chuck on 2017/12/6.
 */

public interface IDb {
    /**
     * 执行Sql语句
     *
     * @param sql
     */
    void execSql(String sql);

    /**
     * 执行查询操作
     *
     * @param sql
     * @return
     */
    ICursor query(String sql);

    /**
     * 关闭数据库
     */
    void close();
}
