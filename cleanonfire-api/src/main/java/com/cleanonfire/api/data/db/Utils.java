package com.cleanonfire.api.data.db;

/**
 * Created by heitorgianastasio on 15/11/17.
 */

final class Utils {
    private Utils(){}

    static String[] parseToStringArray(Object[] objects){
        if (objects == null) return null;
        String[] strings = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            strings[i] = String.valueOf(objects[i]);
        }
        return strings;
    }
}
