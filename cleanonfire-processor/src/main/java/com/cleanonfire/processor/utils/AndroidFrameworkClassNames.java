package com.cleanonfire.processor.utils;

import com.squareup.javapoet.ClassName;

/**
 * Created by heitorgianastasio on 03/10/17.
 */

public final class AndroidFrameworkClassNames {
    private AndroidFrameworkClassNames() {}

    public static ClassName CURSOR = ClassName.get("android.database","Cursor");
    public static ClassName SQLITEDATABASE = ClassName.get("android.database.sqlite","SQLiteDatabase");
    public static ClassName SQLEXCEPTION = ClassName.get("android.database","SQLException");
    public static ClassName CONTEXT = ClassName.get("android.content","Context");
    public static ClassName CONTENT_VALUES = ClassName.get("android.content","ContentValues");
    public static ClassName VIEW = ClassName.get("android.view","View");
}
