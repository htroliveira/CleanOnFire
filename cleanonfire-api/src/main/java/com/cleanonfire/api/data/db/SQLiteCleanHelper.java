package com.cleanonfire.api.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.List;

/**
 * Created by heitorgianastasio on 02/10/17.
 */

public final class SQLiteCleanHelper extends SQLiteOpenHelper {
    private AbstractCleanOnFireDB abstractCleanOnFireDB;

    public SQLiteCleanHelper(AbstractCleanOnFireDB abstractCleanOnFireDB, Context context, String name) {
        super(context, name, null, abstractCleanOnFireDB.getVersion());
        this.abstractCleanOnFireDB = abstractCleanOnFireDB;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String sql : abstractCleanOnFireDB.sqlCreateScript().split(";")) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {

        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        List<Migration> migrations = abstractCleanOnFireDB.getMigrations(i, i1);
        if (!migrations.isEmpty()) {
            for (Migration migration : migrations) {
                migration.apply(sqLiteDatabase);
            }
        } else {
            throw new IllegalStateException(
                    "A schema upgrade from the version "+i+ " to the version "+i1+ " was required, " +
                    "but migrations to this were not found. " +
                    "Please, make sure that you implemented this on your @Database annotated interface."
            );
        }
    }
}
