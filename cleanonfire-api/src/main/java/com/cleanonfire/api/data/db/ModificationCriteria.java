package com.cleanonfire.api.data.db;

/**
 * Created by heitorgianastasio on 03/10/17.
 */

public final class ModificationCriteria {
    public static final ModificationCriteria EMPTY = new ModificationCriteria();
    String selection;
    String[] selectionArgs;

    public static Builder builder() {
        return new Builder();
    }

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

    public static class Builder {
        ModificationCriteria queryCriteria = new ModificationCriteria();

        public Builder setSelection(String selection) {
            queryCriteria.selection = selection;
            return this;
        }

        public Builder setSelectionArgs(String... selectionArgs) {
            queryCriteria.selectionArgs = selectionArgs;
            return this;
        }

        public ModificationCriteria build() {
            return queryCriteria;
        }

    }
}
