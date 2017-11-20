package com.cleanonfire.sample.presentation.main.visualization;

import android.view.View;

import com.cleanonfire.api.presentation.adapter.ViewBinder;

/**
 * Created by heitorgianastasio on 15/11/17.
 */

public class VisibiltyBinder implements ViewBinder<Boolean,View> {
    @Override
    public void bind(Boolean aBoolean, View view) {
            view.setVisibility(aBoolean?View.VISIBLE:View.GONE);
    }
}
