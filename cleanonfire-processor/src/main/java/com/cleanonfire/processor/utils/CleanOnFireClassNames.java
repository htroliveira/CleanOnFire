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
    public static ClassName CLEAN_ON_FIRE_DB = ClassName.get("com.generated.cleanonfire.db","CleanOnFireDB");


    public static ClassName BASE_RV_ADAPTER = ClassName.get("com.cleanonfire.api.presentation.adapter","BaseRecyclerViewAdapter");
    public static ClassName BASE_LIST_ADAPTER = ClassName.get("com.cleanonfire.api.presentation.adapter","BaseListAdapter");
    public static ClassName VIEW_HOLDER = ClassName.get("com.cleanonfire.api.presentation.adapter","ViewHolder");
    public static ClassName RECYCLER_VIEW_HOLDER = ClassName.get("com.cleanonfire.api.presentation.adapter","RecyclerViewHolder");
    public static ClassName VIEW_HOLDER_BINDER = ClassName.get("com.cleanonfire.api.presentation.adapter","ViewHolderBinder");
    public static ClassName ON_ITEM_LONG_CLICK_LISTENER = ClassName.get("com.cleanonfire.api.presentation.adapter","OnItemLongClickListener");
    public static ClassName ON_ITEM_CLICK_LISTENER = ClassName.get("com.cleanonfire.api.presentation.adapter","OnItemClickListener");


}
