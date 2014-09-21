/*
 * Copyright 2014 Augmentum Inc. All rights reserved.
 */

package com.kindazrael.tingweather;

import java.util.List;
import java.util.Map;

import com.crittercism.app.Crittercism;
import com.kindazrael.tingweather.dao.CityDao;
import com.kindazrael.tingweather.util.LogUtil;

public class IWApplication extends android.app.Application {

    public static IWApplication iWApplication = null;
    public static List <Map <String, String>> localCityInfoList = null;

    @ Override
    public void onCreate() {
        super.onCreate();
        iWApplication = this;

        if (!Constants.IS_DEBUG) {
            // Init mccCrashHandler
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());

            // Initizlise Crittercism to handler runtime exception.
            Crittercism.initialize(getApplicationContext(),
                    Constants.CRITTERCISM_APP_ID);
        }
    }

    public static IWApplication getInstance() {
        return iWApplication;
    }

    /**
     * 获取本地的城市信息。
     * 
     * @return
     */
    public static List <Map <String, String>> getLocalCityInfo() {
        if (null == localCityInfoList) {
            localCityInfoList = CityDao.getLocalCityInfo();
            LogUtil.i("Application", "----> getLocalCityInfo");
        }
        return localCityInfoList;
    }
}
