package com.hero.gossipvideo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ltc.lib.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/13.
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter {

    public List<T> mList;
    public Context mContext;
    public LayoutInflater mInflater;

    public BaseRecyclerViewAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<T>();
        mInflater = LayoutInflater.from(context);
    }

    public void setList(List<T> list) {
        if (this.mList != null) this.mList.clear(); // 避免脏数据
        if (list == null || list.size() == 0) return;
        this.mList = list;
    }

    public void setList(T[] list){
        ArrayList<T> arrayList = new ArrayList<T>(list.length);
        for (T t : list) {
            arrayList.add(t);
        }
        setList(arrayList);
    }

    public List<T> getList() {
        return this.mList;
    }

    public void addAll(List<T> list) {
        if (list == null || list.size() == 0) return;
        this.mList.addAll(list);
    }

    public void addAll(int location, List<T> list) {
        if (list == null || list.size() == 0) return;
        this.mList.addAll(location, list);
    }

    public void clean() {
        if (getItemCount() == 0) return;
        mList.clear();
    }

    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return Utils.isEmpty(mList) ? 0 : mList.size();
    }
}
