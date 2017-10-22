package com.cleanonfire.api.data.orm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heitorgianastasio on 03/10/17.
 */

public abstract class BaseCleanDAO<T> {
    protected SQLiteCleanHelper dbHelper;

    public BaseCleanDAO(SQLiteCleanHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    protected abstract String getIdentificationColumnName();

    protected abstract String getTableName();

    protected abstract T parseFromCursor(Cursor cursor);

    protected abstract ContentValues parseToContentValues(T t);

    protected abstract Number getId(T t);

    public List<T> getAll() {
        Cursor cursor = queryFromCriteria(QueryCriteria.EMPTY,dbHelper.getReadableDatabase());
        List<T> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                result.add(parseFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return result;
    }

    public T getById(Number id) {
        QueryCriteria criteria = QueryCriteria
                .builder()
                .setSelection(getIdentificationColumnName().concat(" = ?"))
                .setSelectionArgs(id.toString())
                .build();
        Cursor cursor = queryFromCriteria(criteria,dbHelper.getReadableDatabase());
        if (cursor.moveToFirst()) {
            return parseFromCursor(cursor);
        } else {
            return null;
        }
    }

    public List<T> query(QueryCriteria queryCriteria){
        Cursor cursor = queryFromCriteria(queryCriteria,dbHelper.getReadableDatabase());
        List<T> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                result.add(parseFromCursor(cursor));
            } while (cursor.moveToNext());

        }
        return result;
    }

    public Number save(T t){
        try {
            dbHelper.getWritableDatabase().insertOrThrow(getTableName(),null,parseToContentValues(t));
        } catch (SQLException e) {
            dbHelper.getWritableDatabase().update(getTableName(),parseToContentValues(t),getIdentificationColumnName().concat("=?"),new String[]{getId(t).toString()});
        } catch (Exception e){
            throw  e;
        }
        return getId(t);
    }

    public Number delete(T t){
        return deleteById(getId(t));
    }

    public Number deleteById(Number id){

        dbHelper.getWritableDatabase().delete(getTableName(),getIdentificationColumnName().concat("=?"),new String[]{id.toString()});
        return id;
    }



    private Cursor queryFromCriteria(QueryCriteria criteria, SQLiteDatabase db){
        return db.query(
                criteria.isDistinct(),
                getTableName(),
                criteria.getColumns(),
                criteria.getSelection(),
                criteria.getSelectionArgs(),
                criteria.getGroupBy(),
                criteria.getHaving(),
                criteria.getOrderBy(),
                criteria.getLimit()
        );
    }
}
