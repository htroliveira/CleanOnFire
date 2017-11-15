package com.cleanonfire.api.presentation.adapter.bind;


import android.widget.TextView;

import com.cleanonfire.api.presentation.adapter.ViewBinder;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public class TextViewBinder implements ViewBinder<Object,TextView>{
    @Override
    public void bind(Object o, TextView view) {
        view.setText(String.valueOf(o));
    }
}
