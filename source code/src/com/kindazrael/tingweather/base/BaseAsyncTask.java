/*
 * Copyright 2014 Augmentum Inc. All rights reserved.
 */

package com.kindazrael.tingweather.base;

import android.os.AsyncTask;

public abstract class BaseAsyncTask< T, K, G> extends AsyncTask <T, K, G> {

    protected BaseUICallBack <T, K, G> mccBaseCallBack;

    public BaseAsyncTask (BaseUICallBack <T, K, G> mccBaseCallBack) {
        this.mccBaseCallBack = mccBaseCallBack;
    }

    public BaseAsyncTask () {

    }

    @ Override
    protected abstract G doInBackground(T... arg0);

    @ Override
    protected void onPostExecute(G result) {
        super.onPostExecute(result);
        if (mccBaseCallBack != null) {
            mccBaseCallBack.onPostExecute(result);
        }
    }

    @ Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mccBaseCallBack != null) {
            mccBaseCallBack.onPreExecute();
        }
    }

    @ Override
    protected void onProgressUpdate(K... values) {
        super.onProgressUpdate(values);
        if (mccBaseCallBack != null) {
            mccBaseCallBack.onProgressUpdate(values);
        }
    }
}
