package com.cleanonfire.api.data.db;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heitorgianastasio on 08/10/17.
 */

public abstract class AbstractCleanOnFireDB {

    protected Context context;
    protected SQLiteCleanHelper cleanHelper;

    public AbstractCleanOnFireDB(Context context) {
        this.context = context;
        cleanHelper = new SQLiteCleanHelper(this, context, getDBName());
    }

    private static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    protected abstract String sqlCreateScript();

    protected abstract int getVersion();

    protected String getDBName() {
        return getApplicationName(context).concat("DB");
    }

    public <T> List<T> rawQuery(CleanCursorParser<T> parser, String sql, Object... args) {
        try (SQLiteDatabase db = cleanHelper.getReadableDatabase()) {
            Cursor cursor = db.rawQuery(sql,Utils.parseToStringArray(args));
            List<T> result = new ArrayList<>();
            CleanCursorReader reader = new CleanCursorReader(cursor);
            if (cursor.moveToFirst()) {
                do {
                    result.add(parser.parse(reader));
                } while (cursor.moveToNext());
            }
            return result;
        }
    }


    public interface CleanCursorParser<T> {
        T parse(CleanCursorReader cursorReader);
    }
}
