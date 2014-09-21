/*
 * Copyright 2014 Augmentum Inc. All rights reserved.
 */

package com.kindazrael.tingweather.util;

import android.util.Log;

import com.kindazrael.tingweather.Constants;

public final class LogUtil {

    private LogUtil () {
    }

    public static void d(String tag, String message) {
        if (StringUtil.isEmpty(message)) {
            return;
        }
        if (Constants.IS_DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void d(String tag, String messageTitle, Object object) {
        if (Constants.IS_DEBUG) {
            Log.d(tag, messageTitle + " " + StringUtil.toJson(object));
        }
    }

    public static void e(String tag, String message) {
        if (StringUtil.isEmpty(message)) {
            return;
        }
        if (Constants.IS_DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String messageTitle, Object object) {
        if (Constants.IS_DEBUG) {
            Log.e(tag, messageTitle + " " + StringUtil.toJson(object));
        }
    }

    public static void i(String tag, String message) {
        if (Constants.IS_DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void i(String tag, String messageTitle, Object object) {
        if (Constants.IS_DEBUG) {
            Log.i(tag, messageTitle + " " + StringUtil.toJson(object));
        }
    }

    public static void v(String tag, String message) {
        if (Constants.IS_DEBUG) {
            Log.v(tag, message);
        }
    }

    public static void v(String tag, String messageTitle, Object object) {
        if (Constants.IS_DEBUG) {
            Log.v(tag, messageTitle + " " + StringUtil.toJson(object));
        }
    }

    public static void w(String tag, String message) {
        if (StringUtil.isEmpty(message)) {
            return;
        }
        if (Constants.IS_DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void w(String tag, String messageTitle, Object object) {
        if (Constants.IS_DEBUG) {
            Log.w(tag, messageTitle + " " + StringUtil.toJson(object));
        }
    }

}
