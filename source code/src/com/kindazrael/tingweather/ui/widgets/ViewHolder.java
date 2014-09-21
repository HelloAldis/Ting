/*
 * Copyright 2013 Capital One Inc. All rights reserved.
 */
package com.kindazrael.tingweather.ui.widgets;


import android.util.SparseArray;
import android.view.View;

public class ViewHolder {

    /**
     * Get component from view holder which is in the view
     * 
     * @param view
     * @param id
     * @return
     */
    @ SuppressWarnings ("unchecked")
    public static < T extends View> T get(View view, int id) {
        SparseArray <View> viewHolder = (SparseArray <View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray <View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

}
