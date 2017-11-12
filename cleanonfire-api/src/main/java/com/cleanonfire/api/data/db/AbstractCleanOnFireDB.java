package com.cleanonfire.api.data.db;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.lang.reflect.Constructor;
import java.util.HashMap;

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

    protected abstract String sqlCreateScript();

    protected abstract int getVersion();

    protected String getDBName() {
        return getApplicationName(context).concat("DB");
    }

    private static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }
}
