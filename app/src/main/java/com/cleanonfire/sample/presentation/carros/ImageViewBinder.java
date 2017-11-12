package com.cleanonfire.sample.presentation.carros;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.cleanonfire.api.presentation.adapter.ViewBinder;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public class ImageViewBinder implements ViewBinder<String,ImageView> {
    @Override
    public void bind(String s, ImageView imageView) {
        imageView.setImageDrawable(Drawable.createFromPath(s));
    }
}
