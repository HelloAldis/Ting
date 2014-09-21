/*
 * Copyright 2014 Augmentum Inc. All rights reserved.
 */

package com.kindazrael.tingweather.db;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kindazrael.tingweather.IWApplication;
import com.kindazrael.tingweather.dao.ForecastSummaryResultCacheDao;
import com.kindazrael.tingweather.dao.WeatherLocationDao;
import com.kindazrael.tingweather.util.LogUtil;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DB_NAME = "iWeather.db";
    private static final int VERSION = 5;
    private static DatabaseHelper instance;
    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS %1$s";
    private static Map <String, String> tables;

    public DatabaseHelper (Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @ Override
    public void onCreate(SQLiteDatabase db) {
        if (db == null || tables == null) {
            return;
        }

        for (String table : tables.keySet()) {
            LogUtil.i("TAG", "SQL:" + String.format(tables.get(table), table));
            db.execSQL(String.format(tables.get(table), table));
        }
        LogUtil.i(TAG, "DatabaseHelper ---> Oncreate");
    }

    @ Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db == null) {
            return;
        }
        for (String table : tables.keySet()) {
            db.execSQL(String.format(DROP_TABLE_SQL, table));
        }
        onCreate(db);
        LogUtil.i(TAG, "DatabaseHelper ---> onUpgrade");
    }

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper(IWApplication.getInstance());
        }
        if (tables == null) {
            tables = new HashMap <String, String>();
            tables.put(ForecastSummaryResultCacheDao.TABLE_NAME,
                    ForecastSummaryResultCacheDao.CREATE_TABLE_SQL);
            tables.put(WeatherLocationDao.TABLE_NAME,
                    WeatherLocationDao.CREATE_TABLE_SQL);
        }
        return instance;
    }
}
