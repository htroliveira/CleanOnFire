package com.cleanonfire.api.presentation.adapter;

import android.view.View;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public interface OnItemClickListener<T> {
    void onItemClick(T t, int position, View view);
}
