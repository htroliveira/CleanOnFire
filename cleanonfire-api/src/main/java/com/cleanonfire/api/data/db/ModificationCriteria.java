package com.cleanonfire.api.data.db;

/**
 * Created by heitorgianastasio on 03/10/17.
 */

public final class ModificationCriteria {
    public static final ModificationCriteria EMPTY = new ModificationCriteria();
    String selection;
    String[] selectionArgs;


    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String[] getSelectionArgs() {
        return selectionArgs;
    }

    public void setSelectionArgs(String[] selectionArgs) {
        this.selectionArgs = selectionArgs;
    }


    public static ModificationCriteria create(String selection, Object... selectionArgs) {
        ModificationCriteria criteria = new ModificationCriteria();
        criteria.selection = selection;
        criteria.selectionArgs = Utils.parseToStringArray(selectionArgs);
        return criteria;
    }
}
