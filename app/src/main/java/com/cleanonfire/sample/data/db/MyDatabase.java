package com.cleanonfire.sample.data.db;

import com.cleanonfire.annotations.data.db.Database;
import com.cleanonfire.annotations.data.db.Migrate;
import com.cleanonfire.api.data.db.Migration;

/**
 * Created by heitorgianastasio on 11/11/17.
 */
@Database(
        version = 2,
        dbname = "mydblegal"
)
public interface MyDatabase {
    @Migrate(fromVersion = 1, toVersion = 2)
    Migration MIGRATION_1_TO_2 = database -> {
        database.execSQL("CREATE TABLE M12(ID INTEGER PRIMARY KEY)");
    };

}
