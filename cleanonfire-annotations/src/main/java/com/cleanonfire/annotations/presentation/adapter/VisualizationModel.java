package com.cleanonfire.annotations.presentation.adapter;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public @interface VisualizationModel {
    int layoutId();
    AdapterType adapterType() default AdapterType.LISTVIEW;

    enum AdapterType{
        LISTVIEW, RECYCLERVIEW
    }
}
