
package com.kindazrael.tingweather.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.kindazrael.tingweather.model.ForecastSummaryResult;
import com.kindazrael.tingweather.util.LogUtil;

public class ForecastSummaryResultCacheDao extends DaoBase {

    private static final String TAG = "ForecastSummaryResultCacheDao";

    public static final String TABLE_NAME = "forecast_summary_result_cache";
    public static final String COLUMN_AREA_ID = "area_id";
    public static final String COLUMN_DATA = "data";

    public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %1$S ("
            + COLUMN_AREA_ID
            + " TEXT PRIMARY KEY, "
            + COLUMN_DATA
            + " TEXT NOT NULL" + ")";

    public static ForecastSummaryResult findForecastSummaryResult(String areaId) {
        if (areaId == null) {
            return null;
        }

        ForecastSummaryResult result = null;
        String json = null;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = getDatabase();

            cursor = db.query(TABLE_NAME, new String [] { COLUMN_DATA },
                    COLUMN_AREA_ID + " = ?", new String [] { areaId }, null,
                    null, null);

            if (cursor.moveToNext()) {
                json = cursor.getString(cursor.getColumnIndex(COLUMN_DATA));
            }

        } catch (SQLiteException e) {
            LogUtil.e(TAG, "Failed to get ForecastSummaryResult. Error: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closeDatabase(db);
        }

        if (json != null) {
            result = ForecastSummaryResult.parseJson(json);
        }

        return result;
    }

    public static void saveForecastSummaryResult(String areaId,
            ForecastSummaryResult result) throws SQLiteException {

        if (result == null || areaId == null) {
            return;
        }

        ForecastSummaryResult existingResult = findForecastSummaryResult(areaId);

        if (existingResult == null) {
            insertForecastSummaryResult(result, areaId);
        } else {
            updateForecastSummaryResult(result, areaId);
        }

    }

    private static void insertForecastSummaryResult(
            final ForecastSummaryResult result, final String areaId) {

        executeSQLiteNoQueryOperation(new SQLiteNoQueryOperation() {

            @ Override
            public void execute(SQLiteDatabase database) {
                ContentValues values = contentValuesFromForecastSummaryResult(
                        result, areaId);
                database.insert(TABLE_NAME, null, values);
            }
        });
    }

    private static void updateForecastSummaryResult(
            final ForecastSummaryResult result, final String areaId) {

        executeSQLiteNoQueryOperation(new SQLiteNoQueryOperation() {

            @ Override
            public void execute(SQLiteDatabase database) {
                ContentValues values = contentValuesFromForecastSummaryResult(
                        result, areaId);

                String selection = COLUMN_AREA_ID + " = ?";
                String [] selectionArgs = { areaId };

                database.update(TABLE_NAME, values, selection, selectionArgs);
            }
        });
    }

    private static ContentValues contentValuesFromForecastSummaryResult(
            ForecastSummaryResult result, String areaId) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_AREA_ID, areaId);
        values.put(COLUMN_DATA, result.toJson());

        return values;
    }
}
