package com.cleanonfire.api.presentation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public abstract class RecyclerViewHolder extends RecyclerView.ViewHolder implements ViewHolder {

    public RecyclerViewHolder(View itemView) {
        super(itemView);
    }
}
