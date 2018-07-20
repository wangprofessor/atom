package com.creative.atom.action.sqlite;

/**
 * 对数据库查询返回的包装
 * Created by chuck on 2017/12/5.
 */

public interface ICursor {
    /**
     * 获取总的数据项
     *
     * @return
     */
    int getCount();

    /**
     * 获取总的列
     */
    int getColumnCount();

    /**
     * 获取position
     *
     * @return
     */
    int getPosition();

    /**
     * 是否是第一个数据项
     *
     * @return
     */
    boolean isFirst();

    /**
     * 是否是最后一个数据项
     *
     * @return
     */
    boolean isLast();

    /**
     * 移动到第一条数据项
     *
     * @return
     */
    boolean moveToFirst();

    /**
     * 移动到最后一条数据项
     *
     * @return
     */
    boolean moveToLast();

    /**
     * 移动offset
     *
     * @param offset
     * @return
     */
    boolean move(int offset);

    /**
     * 移动到相应的pos
     *
     * @param pos
     * @return
     */
    boolean moveToPosition(int pos);

    /**
     * 移动到下一数据项
     *
     * @return
     */
    boolean moveToNext();

    /**
     * 移动到前一条数据
     *
     * @return
     */
    boolean moveToPrevious();

    boolean isBeforeFirst();

    boolean isAfterLast();

    /**
     * 根据列名获取列索引
     *
     * @param columnName
     * @return
     */
    int getColumnIndex(String columnName);

    int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException;

    /**
     * 根据列索引获取列名
     *
     * @param columnIndex
     * @return
     */
    String getColumnName(int columnIndex);

    /**
     * 获取所有列名
     *
     * @return
     */
    String[] getColumnNames();

    /**
     * 是否关闭
     *
     * @return
     */
    boolean isClose();

    /**
     * 指定列的值为null时返回true
     *
     * @param columnIndex
     * @return 指定列的值为null时返回true
     */
    boolean isNull(int columnIndex);

    /**
     * 获取String
     *
     * @param columnIndex
     * @return
     */
    String getString(int columnIndex);

    /**
     * 获取short
     *
     * @param columnIndex
     * @return
     */
    short getShort(int columnIndex);

    /**
     * 获取指定列的数据类型
     */
    int getType(int columnIndex);

    /**
     * 获取float
     *
     * @param columnIndex
     * @return
     */
    float getFloat(int columnIndex);

    /**
     * 获取int
     *
     * @param columnIndex
     * @return
     */
    int getInt(int columnIndex);

    /**
     * 获取long
     *
     * @param columnIndex
     * @return
     */
    long getLong(int columnIndex);

    /**
     * 获取double
     *
     * @param columnIndex
     * @return
     */
    double getDouble(int columnIndex);

    void close();
}
