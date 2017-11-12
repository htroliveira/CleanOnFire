package com.cleanonfire.api.presentation.adapter.bind;

import android.widget.CompoundButton;

import com.cleanonfire.api.presentation.adapter.ViewBinder;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public class CompoundViewBinder implements ViewBinder<Boolean,CompoundButton> {
    @Override
    public void bind(Boolean aBoolean, CompoundButton compoundButton) {
        compoundButton.setChecked(aBoolean);
    }
}
