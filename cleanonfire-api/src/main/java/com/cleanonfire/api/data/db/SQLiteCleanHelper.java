package com.cleanonfire.api.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by heitorgianastasio on 02/10/17.
 */

public final class SQLiteCleanHelper extends SQLiteOpenHelper {
    private AbstractCleanOnFireDB abstractCleanOnFireDB;

    public SQLiteCleanHelper(AbstractCleanOnFireDB abstractCleanOnFireDB, Context context, String name) {
        super(context, name, null, 1);
        this.abstractCleanOnFireDB = abstractCleanOnFireDB;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String sql : abstractCleanOnFireDB.sqlCreateScript().split(";")) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
