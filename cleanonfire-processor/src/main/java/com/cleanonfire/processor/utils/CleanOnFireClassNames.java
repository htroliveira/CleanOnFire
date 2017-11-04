package com.cleanonfire.processor.utils;

import com.squareup.javapoet.ClassName;

/**
 * Created by heitorgianastasio on 03/10/17.
 */

public final class CleanOnFireClassNames {
    private CleanOnFireClassNames(){}

    public static ClassName BASE_DAO = ClassName.get("com.cleanonfire.api.data.db","BaseCleanDAO");
    public static ClassName IDENTIFICATION = ClassName.get("com.cleanonfire.api.data.db.BaseCleanDAO","Identification");
    public static ClassName CLEAN_SQLITE_HELPER = ClassName.get("com.cleanonfire.api.data.db","SQLiteCleanHelper");
    public static ClassName ABSTRACT_CLEAN_ON_FIRE_DB = ClassName.get("com.cleanonfire.api.data.db","AbstractCleanOnFireDB");
    public static ClassName QUERY_CRITERIA = ClassName.get("com.cleanonfire.api.data.db","QueryCriteria");
    public static ClassName CLEAN_ON_FIRE_DB = ClassName.get("com.generated.cleanonfire.db","CleanOnFireDB");
}
