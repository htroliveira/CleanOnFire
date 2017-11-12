package com.cleanonfire.api.data.db;

import java.util.Collections;
import java.util.stream.Stream;

/**
 * Created by heitorgianastasio on 03/10/17.
 */

public final class QueryCriteria {
    String[] columns;
    String selection;
    String[] selectionArgs;
    String groupBy;
    String orderBy;
    String having;
    String limit;
    boolean distinct = false;

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
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

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getHaving() {
        return having;
    }

    public void setHaving(String having) {
        this.having = having;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public static final QueryCriteria EMPTY = new QueryCriteria();

    public static Builder builder(){return new Builder();}

    public static class Builder{
        QueryCriteria queryCriteria = new QueryCriteria();
        public Builder setColumns(String... columns) {
            queryCriteria.columns = columns;
            return this;
        }

        public Builder setSelection(String selection) {
            queryCriteria.selection = selection;
            return this;
        }

        public Builder setSelectionArgs(Object... selectionArgs) {
            String[] args = new String[selectionArgs.length];
            for (int i = 0; i < selectionArgs.length; i++) {
                args[i] = String.valueOf(selectionArgs[i]);
            }
            queryCriteria.selectionArgs = args;
            return this;
        }
        public Builder setSelectionArgs(String... selectionArgs) {
            queryCriteria.selectionArgs = selectionArgs;
            return this;
        }

        public Builder setGroupBy(String groupBy) {
            queryCriteria.groupBy = groupBy;
            return this;
        }

        public Builder setOrderBy(String orderBy) {
            queryCriteria.orderBy = orderBy;
            return this;
        }

        public Builder setHaving(String having) {
            queryCriteria.having = having;
            return this;
        }

        public Builder setLimit(String limit) {
            queryCriteria.limit = limit;
            return this;
        }

        public Builder setDistinct(boolean distinct) {
            queryCriteria.distinct = distinct;
            return this;
        }

        public QueryCriteria build(){
            return queryCriteria;
        }

    }
}
