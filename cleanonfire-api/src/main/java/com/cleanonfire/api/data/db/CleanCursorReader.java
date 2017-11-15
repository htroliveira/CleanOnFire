package com.cleanonfire.api.data.db;

import android.database.Cursor;

/**
 * Created by heitorgianastasio on 15/11/17.
 */

public final class CleanCursorReader {
    private Cursor cursor;

    public CleanCursorReader(Cursor cursor) {
        this.cursor = cursor;
    }

    public int getCount() {
        return cursor.getCount();
    }

    public int getPosition() {
        return cursor.getPosition();
    }

    public byte[] getBlob(String columnName) {
        return cursor.getBlob(cursor.getColumnIndex(columnName));
    }

    public String getString(String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public short getShort(String columnName) {
        return cursor.getShort(cursor.getColumnIndex(columnName));
    }

    public int getInt(String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public long getLong(String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    public float getFloat(String columnName) {
        return cursor.getFloat(cursor.getColumnIndex(columnName));
    }

    public double getDouble(String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }

    public boolean isNull(String columnName) {
        return cursor.isNull(cursor.getColumnIndex(columnName));
    }

}
