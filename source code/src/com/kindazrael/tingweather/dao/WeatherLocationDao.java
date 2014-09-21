
package com.kindazrael.tingweather.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.kindazrael.tingweather.model.WeatherLocation;
import com.kindazrael.tingweather.util.LogUtil;

public class WeatherLocationDao extends DaoBase {

    private static final String TAG = WeatherLocationDao.class.getName();
    public static final String TABLE_NAME = "location";
    public static final String COLUMN_AREAID = "areaId";
    public static final String COLUMN_PROVINCE = "province";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_DISTRICT = "district";
    public static final String COLUMN_SPELL = "spell";
    public static final String COLUMN_SORTORDER = "sortOrder";

    public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %1$s (" + COLUMN_AREAID
            + " TEXT PRIMARY KEY, " + COLUMN_PROVINCE + " TEXT , " + COLUMN_CITY + " TEXT NOT NULL , "
            + COLUMN_DISTRICT + " TEXT , " + COLUMN_SPELL + " TEXT NOT NULL , " + COLUMN_SORTORDER + " INTEGER" + ")";

    /**
     * Create a location
     * 
     * @param location
     * @return
     */
    public static long insertLocation(WeatherLocation location) {
        long tableCount = getTableCount();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AREAID, location.areaId);
        values.put(COLUMN_PROVINCE, location.province);
        values.put(COLUMN_CITY, location.city);
        values.put(COLUMN_DISTRICT, location.district);
        values.put(COLUMN_SPELL, location.spell);
        values.put(COLUMN_SORTORDER, tableCount);

        long rowId = getDatabase().insert(TABLE_NAME, null, values);
        LogUtil.i(TAG, "Insert location, rowId:" + rowId);
        return rowId;
    }

    /**
     * Sort by Name
     * 
     * @param
     * @return
     */
    public static List <WeatherLocation> sortByName() {
        List<WeatherLocation> allWeatherLocationInfo = new ArrayList <WeatherLocation>();
        Cursor cursor = null;
        try {
            cursor = getDatabase().query(
                    TABLE_NAME,
                    new String [] { COLUMN_SORTORDER, COLUMN_AREAID, COLUMN_CITY, COLUMN_DISTRICT, COLUMN_PROVINCE,
                            COLUMN_SPELL }, null, null, null, null, COLUMN_CITY);
            if (null != cursor && cursor.moveToFirst()) {
                do {
                    WeatherLocation location = new WeatherLocation();
                    location.areaId = cursor.getString(cursor.getColumnIndex(COLUMN_AREAID));
                    location.province = cursor.getString(cursor.getColumnIndex(COLUMN_PROVINCE));
                    location.city = cursor.getString(cursor.getColumnIndex(COLUMN_CITY));
                    location.district = cursor.getString(cursor.getColumnIndex(COLUMN_DISTRICT));
                    location.spell = cursor.getString(cursor.getColumnIndex(COLUMN_SPELL));
                    location.sortOrder = cursor.getInt(cursor.getColumnIndex(COLUMN_SORTORDER));
                    allWeatherLocationInfo.add(location);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return allWeatherLocationInfo;
    }

    /**
     * Delete location by id.
     * 
     * @param id
     * @return
     */
    public static long deleteLocaitonById(String id) {
        int result = -1;
        int sortOrder = -1;
        try {
            sortOrder = getSortOderById(id);
            if (sortOrder != -1) {
                result = getDatabase().delete(TABLE_NAME, COLUMN_AREAID + "= ?", new String [] { id });
                if (result != -1) {
                    List <WeatherLocation> allLocationInfo = getAllLoaction();
                    for (; sortOrder < allLocationInfo.size(); sortOrder++) {
                        if (updateLocaitonSortOrderFieldById(allLocationInfo.get(sortOrder).areaId, sortOrder - 1) == -1) {
                            LogUtil.e(TAG, "Update sortOrder id:%d failed", sortOrder);
                            result = -1;
                            break;
                        } else {
                            result = 0;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return result;

    }

    /**
     * Get sort order by area id.
     * 
     * @param area
     *            id
     * @return sort order
     */
    public static int getSortOderById(String areaId) {
        int sortOrder = -1;
        Cursor cursor = null;
        try {
            cursor = getDatabase().query(TABLE_NAME, new String [] { COLUMN_SORTORDER }, COLUMN_AREAID + " = ?",
                    new String [] { areaId }, null, null, null);
            if (null != cursor && cursor.moveToFirst()) {
                // Exist
                sortOrder = cursor.getInt(cursor.getColumnIndex(COLUMN_SORTORDER));
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return sortOrder;
    }
    
    /**
     * Check city exist
     * 
     * @param areaId
     *            areaId
     * @return boolean
     */
    public static boolean checkCity(String areaId) {
        boolean result = true;
        Cursor cursor = null;
        try {
            cursor = getDatabase().query(TABLE_NAME, new String [] { COLUMN_SORTORDER }, COLUMN_AREAID + " = ?",
                    new String [] { areaId }, null, null, null);
            if (null != cursor && cursor.moveToFirst()) {
                result = true;
            }else{
                result = false;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Delete location by id.
     * 
     * @param id
     * @return
     */
    public static long updateLocaitonSortOrderFieldById(String id, int sortOrderValue) {
        int result = -1;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SORTORDER, sortOrderValue);
            result = getDatabase().update(TABLE_NAME, contentValues, COLUMN_AREAID + "= ?", new String [] { id });
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return result;

    }

    /**
     * Is location existing in database search by id.
     * 
     * @param id
     * @return
     */
    public static long isLocationExist(String areaId) {
        int result = -1;
        Cursor cursor = null;
        try {
            cursor = getDatabase().query(TABLE_NAME, new String [] { COLUMN_CITY }, COLUMN_AREAID + " = ?",
                    new String [] { areaId }, null, null, null);
            if (null != cursor && cursor.moveToFirst()) {
                // Exist
                result = -1;
            } else {
                // Not exist
                result = 0;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Get table count.
     * 
     * @return
     */
    public static long getTableCount() {
        String sql = "select count(*) from " + TABLE_NAME;
        long count = -1;
        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(sql, null);
            if (null != cursor && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    /**
     * Get all locations.
     * 
     * @return
     */
    public static List <WeatherLocation> getAllLoaction() {
        String sql = "select * from " + TABLE_NAME + " ORDER  By " + COLUMN_SORTORDER + " DESC";
        List <WeatherLocation> locationList = new ArrayList <WeatherLocation>();
        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(sql, null);
            if (null != cursor && cursor.moveToFirst()) {
                do {
                    WeatherLocation location = new WeatherLocation();
                    location.areaId = cursor.getString(cursor.getColumnIndex(COLUMN_AREAID));
                    location.province = cursor.getString(cursor.getColumnIndex(COLUMN_PROVINCE));
                    location.city = cursor.getString(cursor.getColumnIndex(COLUMN_CITY));
                    location.district = cursor.getString(cursor.getColumnIndex(COLUMN_DISTRICT));
                    location.spell = cursor.getString(cursor.getColumnIndex(COLUMN_SPELL));
                    location.sortOrder = cursor.getInt(cursor.getColumnIndex(COLUMN_SORTORDER));
                    locationList.add(location);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return locationList;
    }

    /**
     * Get all locations.
     * 
     * @return
     */
    public static WeatherLocation getLoactionByNameCN(String cityNameCN) {
        String sql = "select * from " + TABLE_NAME + " where " + COLUMN_CITY + " = " + cityNameCN;
        WeatherLocation location = new WeatherLocation();
        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(sql, null);
            if (null != cursor && cursor.moveToFirst()) {
                do {
                    location.areaId = cursor.getString(cursor.getColumnIndex(COLUMN_AREAID));
                    location.province = cursor.getString(cursor.getColumnIndex(COLUMN_PROVINCE));
                    location.city = cursor.getString(cursor.getColumnIndex(COLUMN_CITY));
                    location.district = cursor.getString(cursor.getColumnIndex(COLUMN_DISTRICT));
                    location.spell = cursor.getString(cursor.getColumnIndex(COLUMN_SPELL));
                    location.sortOrder = cursor.getInt(cursor.getColumnIndex(COLUMN_SORTORDER));
                    break;
                } while (cursor.moveToNext());
            } else {
                location = null;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return location;
    }

    /**
     * Update location.
     * 
     * @param location
     * @return
     */
    public static int updateLocation(WeatherLocation location) {
        int result = -1;
        if (isLocationExist(location.areaId) == 0) {
            result = (int) insertLocation(location);
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_AREAID, location.areaId);
            contentValues.put(COLUMN_PROVINCE, location.province);
            contentValues.put(COLUMN_CITY, location.city);
            contentValues.put(COLUMN_DISTRICT, location.district);
            contentValues.put(COLUMN_SPELL, location.spell);
            contentValues.put(COLUMN_SORTORDER, location.sortOrder);
            try {
                result = getDatabase().update(TABLE_NAME, contentValues, COLUMN_AREAID + "= ?",
                        new String [] { location.areaId });
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
            }
            return -2;
        }
        return result;
    }

}
