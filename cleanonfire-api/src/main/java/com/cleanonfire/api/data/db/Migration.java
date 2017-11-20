package com.cleanonfire.api.data.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by heitorgianastasio on 19/11/17.
 */

public interface Migration {
    void apply(SQLiteDatabase database);
}
