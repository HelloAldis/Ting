
package com.kindazrael.tingweather.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kindazrael.tingweather.IWApplication;
import com.kindazrael.tingweather.db.AssetsDatabaseHelper;
import com.kindazrael.tingweather.util.LogUtil;

public class CityDao {

    private static final String TAG = "CityDao";
    // City column name in assets
    public static final String ASSETS_CITY_TABLE_COLUMN_LOCATIONID = "LocationID";
    public static final String ASSETS_CITY_TABLE_COLUMN_CITY_NAME_CN = "CitiesNameCN";
    public static final String ASSETS_CITY_TABLE_COLUMN_CITY_NAME_EN = "CitiesNameEN";

    /**
     * Search city info from local database though city Chinese name or ciry
     * pinyin name.
     * 
     * @param cityName
     *            - City name
     * @return
     */
    public static List <Map <String, String>> getCityInfoByCityName(String cityName) {
        List <Map <String, String>> cityList = new ArrayList <Map <String, String>>();

        if (null == cityName) {
            return cityList;
        }
        List <Map <String, String>> localCityList = IWApplication
                .getLocalCityInfo();
        for (Map <String, String> cityInfo : localCityList) {
            if (null != cityInfo) {
                String cityNameEn = cityInfo.get(ASSETS_CITY_TABLE_COLUMN_CITY_NAME_EN);
                String cityNameCN = cityInfo.get(ASSETS_CITY_TABLE_COLUMN_CITY_NAME_CN);
                if (cityName.equalsIgnoreCase(cityNameCN) || cityName.equalsIgnoreCase(cityNameEn)) {
                    cityList.add(cityInfo);
                    break;
                }
            }
        }
        return cityList;
    }

    /**
     * Search city info from local database though city Chinese name or ciry
     * pinyin name.
     * 
     * @param cityName
     *            - City name
     * @return
     */
    public static List <Map <String, String>> getCityInfoByCityNameForuzzy(String cityName) {
        List <Map <String, String>> cityList = new ArrayList <Map <String, String>>();
        if (null == cityName) {
            return cityList;
        }
        List <Map <String, String>> localCityList = IWApplication
                .getLocalCityInfo();
        for (Map <String, String> cityInfo : localCityList) {
            if (null != cityInfo) {
                String cityNameEn = cityInfo.get(ASSETS_CITY_TABLE_COLUMN_CITY_NAME_EN).toUpperCase(Locale.getDefault());
                String cityNameCN = cityInfo.get(ASSETS_CITY_TABLE_COLUMN_CITY_NAME_CN);
                if (cityNameCN.contains(cityName) || cityNameEn.contains(cityName.toUpperCase(Locale.getDefault()))) {
                    cityList.add(cityInfo);
                }
            }
        }
        return cityList;
    }

    /**
     * 从Assets里的城市数据库获取所有的城市信息，城市信息存放再application中，要使用城市信息可以去哪里拿。
     * 
     * @return
     */
    public static List <Map <String, String>> getLocalCityInfo() {
        List <Map <String, String>> cityList = new ArrayList <Map <String, String>>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            AssetsDatabaseHelper assetsDatabaseHelper = AssetsDatabaseHelper.getInstance();
            db = assetsDatabaseHelper.createDataBase();
            cursor = db.rawQuery("Select * from Cities", null);
            if (null != cursor && cursor.moveToFirst()) {
                do {
                    Map <String, String> cityMap = new HashMap <String, String>();
                    cityMap.put(ASSETS_CITY_TABLE_COLUMN_LOCATIONID,
                            cursor.getString(cursor.getColumnIndex(ASSETS_CITY_TABLE_COLUMN_LOCATIONID)));
                    cityMap.put(ASSETS_CITY_TABLE_COLUMN_CITY_NAME_CN,
                            cursor.getString(cursor.getColumnIndex(ASSETS_CITY_TABLE_COLUMN_CITY_NAME_CN)));
                    cityMap.put(ASSETS_CITY_TABLE_COLUMN_CITY_NAME_EN,
                            cursor.getString(cursor.getColumnIndex(ASSETS_CITY_TABLE_COLUMN_CITY_NAME_EN)));
                    cityList.add(cityMap);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return cityList;
    }
}
