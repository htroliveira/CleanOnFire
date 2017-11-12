package com.cleanonfire.api.presentation.adapter;

import android.content.Context;
import android.view.View;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public interface ViewHolderBinder<T, VH extends ViewHolder> {
    void bind(VH viewHolder, T t);
}
