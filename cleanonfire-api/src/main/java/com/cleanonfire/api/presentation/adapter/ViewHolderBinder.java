package com.cleanonfire.api.presentation.adapter;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public interface ViewHolderBinder<T, VH extends ViewHolder> {
    void bind(VH viewHolder, T t);
}
