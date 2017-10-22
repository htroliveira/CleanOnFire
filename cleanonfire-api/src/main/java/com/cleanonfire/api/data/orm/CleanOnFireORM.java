package com.cleanonfire.api.data.orm;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Created by heitorgianastasio on 08/10/17.
 */

public final class CleanOnFireORM {
    private static HashMap<Class<? extends BaseCleanDAO>, Constructor<? extends BaseCleanDAO>> DAO_CONSTRUCTORS = new HashMap<>();
    private static SQLiteCleanHelper SQLITE_CLEAN_HELPER;

    public static <T extends BaseCleanDAO> T getDao(Class<T> daoClass) {
        Constructor<T> constructor = findDAOConstructor(daoClass);

        try {
            return constructor.newInstance(SQLITE_CLEAN_HELPER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static <T extends BaseCleanDAO> Constructor<T> findDAOConstructor(Class<T> daoClass) {

        Constructor<T> daoConstructor = (Constructor<T>) DAO_CONSTRUCTORS.get(daoClass);

        if (daoConstructor != null)
            return daoConstructor;

        if (daoClass.getName().startsWith("android.") || daoClass.getName().startsWith("java.")) {
            return null;
        }

        try {
            daoConstructor = daoClass.getConstructor(SQLiteCleanHelper.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();

        }
        DAO_CONSTRUCTORS.put(daoClass,daoConstructor);
        return daoConstructor;

    }


    public static void init(Context context) {
        SQLITE_CLEAN_HELPER = new SQLiteCleanHelper(context, context.getApplicationInfo().name);
    }

}
