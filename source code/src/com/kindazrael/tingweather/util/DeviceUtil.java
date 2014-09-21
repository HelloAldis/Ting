/*
 * Copyright 2014 Augmentum Inc. All rights reserved.
 */

package com.kindazrael.tingweather.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.kindazrael.tingweather.IWApplication;

public final class DeviceUtil {

    private DeviceUtil () {

    }

    public static boolean isConnectingToInternet() {
        if (isWifi() || isMobileNetwork()) {
            return true;
        }

        return false;
    }

    public static boolean isWifi() {
        ConnectivityManager connectMgr = (ConnectivityManager) IWApplication
                .getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static boolean isMobileNetwork() {
        ConnectivityManager connectMgr = (ConnectivityManager) IWApplication
                .getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }

        return false;
    }

    /**
     * 手机网络进行详细区分：
     * info.getSubtype() 这里使用 getSubtype()，不是 getType()，getType()返回的
     * 是0，或者1，是区分是手机网络还是wifi
     * info.getSubtype()取值列表如下：
     * NETWORK_TYPE_CDMA 网络类型为CDMA
     * NETWORK_TYPE_EDGE 网络类型为EDGE
     * NETWORK_TYPE_EVDO_0 网络类型为EVDO0
     * NETWORK_TYPE_EVDO_A 网络类型为EVDOA
     * NETWORK_TYPE_GPRS 网络类型为GPRS
     * NETWORK_TYPE_HSDPA 网络类型为HSDPA
     * NETWORK_TYPE_HSPA 网络类型为HSPA
     * NETWORK_TYPE_HSUPA 网络类型为HSUPA
     * NETWORK_TYPE_UMTS 网络类型为UMTS
     * 联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EDGE，电信的2G为CDMA，电信 的3G为EVDO
     */
    public static boolean is2G() {
        // TODO
        return false;
    }

    public static boolean is3G() {
        // TODO
        return false;
    }

}
