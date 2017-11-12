package com.cleanonfire.api.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public abstract class BaseListAdapter<T, B extends ViewHolderBinder<T,VH>, VH extends ViewHolder> extends BaseAdapter {
    Context context;
    List<T> list;
    B binder;

    public BaseListAdapter(List<T> list, Context context, B binder) {
        this.context = context;
        this.list = list;
        this.binder = binder;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected abstract int getLayoutId();

    protected abstract VH createViewHolder(View root);

    protected void onBindViewHolder(VH holder,int position){
        binder.bind(holder,list.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        VH holder;

        if( convertView == null) {
            view = LayoutInflater.from(context).inflate(getLayoutId(),parent,false);
            holder = createViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (VH) view.getTag();
        }
        onBindViewHolder(holder,position);
        return view;
    }
}
