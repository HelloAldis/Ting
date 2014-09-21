/*
 * Copyright 2014 Augmentum Inc. All rights reserved.
 */

package com.kindazrael.tingweather.util;

import java.util.Map;

import com.google.gson.Gson;

public final class StringUtil {

    private StringUtil () {
    }

    public static String encodeUrl(String requestUrl,
            Map <String, Object> params) {
        StringBuilder url = new StringBuilder(requestUrl);
        if (url.indexOf("?") < 0)
            url.append('?');
        if (params != null) {
            for (String name : params.keySet()) {
                url.append('&');
                url.append(name);
                url.append('=');
                url.append(String.valueOf(params.get(name)));
            }
        }
        String urlStr = url.toString().replace(" ", "+");
        urlStr = urlStr.replace("?&", "?");
        return urlStr;
    }

    private static Gson gson;

    static {
        gson = new Gson();
    }

    public static Object fromJson(String josnStr, Class <?> cls) {
        return gson.fromJson(josnStr, cls);
    }

    public static String toJson(Object object) {
        if (object == null) {
            return "";
        }
        return gson.toJson(object);
    }

    public static boolean isEmpty(String message) {
        if (message == null || "".equals(message.trim())) {
            return true;
        }
        return false;
    }

    public static String formartTemperature(double temperature) {
        return (int)temperature + "";
    }
}
