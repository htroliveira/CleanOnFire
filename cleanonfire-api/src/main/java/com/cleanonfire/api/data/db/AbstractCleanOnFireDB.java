package com.cleanonfire.api.data.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.util.Base64.DEFAULT;
import static android.util.Base64.encode;

/**
 * Created by heitorgianastasio on 08/10/17.
 */

public abstract class AbstractCleanOnFireDB {

    protected Context context;
    protected SQLiteCleanHelper cleanHelper;
    protected Map<VersionRange, Migration> migrations = new HashMap<>();

    public AbstractCleanOnFireDB(Context context) {
        this.context = context;
        cleanHelper = new SQLiteCleanHelper(this, context, getDBName());
    }

    private static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    protected List<Migration> getMigrations(int fromVersion, int toVersion) {
        if (toVersion - fromVersion > 1 && migrations.keySet().contains(VersionRange.create(fromVersion, toVersion))) {
            return Collections.singletonList(migrations.get(VersionRange.create(fromVersion, toVersion)));
        } else {
            List<Migration> requiredMigrations = new ArrayList<>();
            for (int i = fromVersion; i < toVersion; i++) {
                Migration migration = migrations.get(VersionRange.create(i, i + 1));
                if (migration != null)
                    requiredMigrations.add(migration);
            }
            if (requiredMigrations.size() >= toVersion - fromVersion)
                return requiredMigrations;
            else
                return Collections.emptyList();
        }
    }

    protected abstract String sqlCreateScript();

    protected abstract int getVersion();

    protected String getDBName() {
        return getApplicationName(context).concat("DB");
    }

    protected void addMigration(Migration migration, int from, int to) {
        migrations.put(VersionRange.create(from, to), migration);
    }

    public <T> List<T> rawQuery(CleanCursorParser<T> parser, String sql, Object... args) {
        try (SQLiteDatabase db = cleanHelper.getReadableDatabase()) {
            Cursor cursor = db.rawQuery(sql, Utils.parseToStringArray(args));
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

    public static class VersionRange {
        private int from;
        private int to;

        private VersionRange() {
        }

        public static VersionRange create(int from, int to) {
            VersionRange range = new VersionRange();
            range.from = from;
            range.to = to;
            return range;
        }

        @Override
        public int hashCode() {
            return ByteBuffer.wrap(encode(toString().getBytes(), DEFAULT)).getInt();
        }

        @SuppressLint("DefaultLocale")
        @Override
        public String toString() {
            return String.format("[form=%d,to=%d]", from, to);
        }

        @Override
        public boolean equals(Object obj) {
            return obj.getClass().equals(getClass()) && this.toString().equals(obj.toString());
        }
    }
}
