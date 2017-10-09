package com.cleanonfire.api.data.orm;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heitorgianastasio on 08/10/17.
 */

public final class CleanOnFireORM {
    static Map<Class<?>,Constructor<? extends BaseCleanDAO<?>>> daoMap = new HashMap<>();

    private static  Context CONTEXT;
    private static  SQLiteCleanHelper SQLITE_CLEAN_HELPER;

    public static <T> BaseCleanDAO<T> getDao(Class<T> entityClass){
        Constructor<? extends BaseCleanDAO<T>> constructor = findDAOConstructor(entityClass);

        try {
            return constructor.newInstance(SQLITE_CLEAN_HELPER);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        return null;
    }

    private static <T> Constructor<? extends BaseCleanDAO<T>> findDAOConstructor(Class<T> entityClass){
        Constructor<? extends BaseCleanDAO<T>> ctor = (Constructor<? extends BaseCleanDAO<T>>) daoMap.get(entityClass);
        if(ctor!=null) return ctor;

        String clsName = entityClass.getName();
        if (clsName.startsWith("android.") || clsName.startsWith("java.")) {
            return null;
        }

        try{
            Class<? extends BaseCleanDAO<T>> baseDaoClass =
                    (Class<? extends BaseCleanDAO<T>>)
                            entityClass.getClassLoader().loadClass(clsName+"CleanDAO");
            ctor = baseDaoClass.getConstructor(SQLiteCleanHelper.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        daoMap.put(entityClass,ctor);
        return ctor;

    }


    public static <T> void lazyFetch(List<T> list, Class<T> type){


    }

    public static <T> void lazyFetch(T item, Class<T> type){

    }

    public static void init(Context context){
        CleanOnFireORM.CONTEXT = context;
        SQLITE_CLEAN_HELPER = new SQLiteCleanHelper(context, context.getApplicationInfo().name);
    }

}
