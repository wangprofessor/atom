package com.creative.atom.action.sqlite.jdbc;

import com.creative.atom.action.sqlite.ICursor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by chuck on 2017/12/11.
 */

class JdbcCursor implements ICursor {
    private ResultSet set;
    private ResultSetMetaData metaData;

    JdbcCursor(ResultSet set) {
        this.set = set;
        try {
            this.metaData = set.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        moveToLast();
        try {
            int count = set.getRow();
            moveToFirst();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int getColumnCount() {
        try {
            return metaData.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int getPosition() {
        try {
            return set.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean isFirst() {
        try {
            return set.isFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isLast() {
        try {
            return set.isLast();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean moveToFirst() {
        try {
            return set.first();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean moveToLast() {
        try {
            return set.last();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean move(int offset) {
        try {
            return set.relative(offset);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean moveToPosition(int pos) {
        try {
            return set.absolute(pos);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean moveToNext() {
        try {
            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean moveToPrevious() {
        try {
            return set.previous();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isBeforeFirst() {
        try {
            return set.isBeforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isAfterLast() {
        try {
            return set.isAfterLast();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getColumnIndex(String columnName) {
        try {
            return set.findColumn(columnName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public String getColumnName(int columnIndex) {
        try {
            return metaData.getColumnName(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String[] getColumnNames() {
        try {
            int count = metaData.getColumnCount();
            String[] columnNames = new String[count];
            for (int i = 0; i < count; i++) {
                columnNames[i] = metaData.getColumnName(i);
            }
            return columnNames;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new String[0];
    }

    @Override
    public boolean isClose() {
        try {
            return set.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean isNull(int columnIndex) {
        try {
            return set.getObject(columnIndex) == null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public String getString(int columnIndex) {
        try {
            return set.getString(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public short getShort(int columnIndex) {
        try {
            return set.getShort(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int getType(int columnIndex) {
        return 0;
    }

    @Override
    public float getFloat(int columnIndex) {
        try {
            return set.getFloat(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1f;
    }

    @Override
    public int getInt(int columnIndex) {
        try {
            return set.getInt(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public long getLong(int columnIndex) {
        try {
            return set.getLong(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1L;
    }

    @Override
    public double getDouble(int columnIndex) {
        try {
            return set.getDouble(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1.0;
    }

    @Override
    public void close() {
        try {
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
