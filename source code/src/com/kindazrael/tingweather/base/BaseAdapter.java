/*
 * Copyright 2014 Augmentum Inc. All rights reserved.
 */

package com.kindazrael.tingweather.base;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter< T> extends android.widget.BaseAdapter {

    protected List <T> data = new ArrayList <T>();

    // Adapter负责数据的取，改，和增加,删除，这是他的职责
    public List <T> getData() {
        return data;
    }

    public void setData(List <T> data) {
        this.data = data;
        if (this.data == null) {
            this.data = new ArrayList <T>();
        }
        // 使用此方法通知List view, 数据发生了变化，你要渲染一下界面
        this.notifyDataSetChanged();
    }

    public void updateData(T item) {

    }

    public void addData(T item) {
        data.add(item);
        this.notifyDataSetChanged();
    }

    public void removeData(T item) {
        data.remove(item);
        this.notifyDataSetChanged();
    }

    public void removeData(int position) {
        data.remove(position);
        this.notifyDataSetChanged();
    }

    // 返回数据条数，这个条数将决定getView 执行的次数
    @ Override
    public int getCount() {
        return data.size();
    }

    // 根据位置信息返回这个位置上的数据，position从0开始，list中的第一个位置信息为0
    @ Override
    public T getItem(int position) {
        if (!data.isEmpty()) {
            return data.get(position);
        }
        return null;
    }

}
