package com.cleanonfire.api.presentation.adapter;

import android.view.View;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public interface ViewBinder<T,V extends View> {
    void bind(T t, V v);
}
