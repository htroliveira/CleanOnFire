package com.cleanonfire.api.presentation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public abstract class BaseRecyclerViewAdapter<T, B extends ViewHolderBinder<T,VH>, VH extends RecyclerViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<T> list;
    protected Context context;
    protected B binder;
    protected OnItemClickListener<T> onItemClickListener;
    protected OnItemLongClickListener<T> onItemLongClickListener;

    public BaseRecyclerViewAdapter(List<T> list, Context context, B binder) {
        this.list = list;
        this.context = context;
        this.binder = binder;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(context).inflate(getLayoutId(),parent,false);
        return createViewHolder(view);
    }

    protected abstract int getLayoutId();

    protected abstract VH createViewHolder(View root);


    @Override
    public void onBindViewHolder(VH holder, int position) {
        binder.bind(holder,list.get(position));
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(list.get(position),position,holder.itemView));
        }
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> onItemLongClickListener.onItemLongClick(list.get(position),position,holder.itemView));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


}
