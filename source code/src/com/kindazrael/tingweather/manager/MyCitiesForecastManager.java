
package com.kindazrael.tingweather.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.widget.Toast;

import com.kindazrael.tingweather.IWApplication;
import com.kindazrael.tingweather.common.Callback;
import com.kindazrael.tingweather.common.Error;
import com.kindazrael.tingweather.dao.CityDao;
import com.kindazrael.tingweather.dao.ForecastSummaryResultCacheDao;
import com.kindazrael.tingweather.dao.WeatherLocationDao;
import com.kindazrael.tingweather.model.ForecastSummaryResult;
import com.kindazrael.tingweather.model.MyCitiesForecastSummaryResult;
import com.kindazrael.tingweather.model.WeatherLocation;
import com.kindazrael.tingweather.util.LogUtil;
import com.kindazrael.tingweather.util.StringUtil;

public class MyCitiesForecastManager {

    private static final String LOG_TAG_STRING = MyCitiesForecastManager.class.getName();

    /**
     * Request MyCitiesForecastSummary get all the city weather information just
     * in the success block.
     * add / update data source can using this method
     * 
     * @param cityNameCN
     *            - City name CN just get from UI
     * @return
     */
    public static void requestMyCitiesForecastSummary(final String cityNameCN,
            final Callback <MyCitiesForecastSummaryResult, Error> callback) {
        if (StringUtil.isEmpty(cityNameCN) || callback == null) {
            throw new IllegalArgumentException("citiy name and callback must not be NULL.");
        }

        final MyCitiesForecastSummaryResult myCitiesForecastSummaryResult = new MyCitiesForecastSummaryResult();
        myCitiesForecastSummaryResult.weatherLocation = new WeatherLocation();
        // Fill in weatherlocation data
        List <Map <String, String>> allCityInfo = null;
        Map <String, String> myCityInfo = null;

        allCityInfo = CityDao.getCityInfoByCityName(cityNameCN);
        if (allCityInfo.isEmpty()) {
            LogUtil.e(LOG_TAG_STRING, "No Data from selected city");
            // Show toast for tip user
            Toast.makeText(IWApplication.getInstance().getApplicationContext(), "所选城市无天气信息", Toast.LENGTH_LONG)
            .show();
            return;
        }
        myCityInfo = allCityInfo.get(0);
        myCitiesForecastSummaryResult.weatherLocation.areaId = myCityInfo
                .get(CityDao.ASSETS_CITY_TABLE_COLUMN_LOCATIONID);
        myCitiesForecastSummaryResult.weatherLocation.spell = myCityInfo
                .get(CityDao.ASSETS_CITY_TABLE_COLUMN_CITY_NAME_EN);
        myCitiesForecastSummaryResult.weatherLocation.city = cityNameCN;

        // Request weather information
        WeatherForcastManager.requestForecastSummary(myCitiesForecastSummaryResult.weatherLocation,
                new Callback <ForecastSummaryResult, Error>() {

                    @ Override
                    public void success(ForecastSummaryResult result) {
                        LogUtil.d(LOG_TAG_STRING, "requestForecastSummary success");
                        LogUtil.d(LOG_TAG_STRING, StringUtil.toJson(result));
                        // Save weather location when api call success. And throw the all summary result to Activity.
                        myCitiesForecastSummaryResult.forecastSummaryResult = result;
                       int code  =  WeatherLocationDao.updateLocation(myCitiesForecastSummaryResult.weatherLocation);
                        if(-2 == code){ // 返回-2意味着该城市已经被添加
                            Toast.makeText(IWApplication.getInstance().getApplicationContext(), "所选城市已经被添加", Toast.LENGTH_LONG)
                            .show();
                        } else {
                            callback.success(myCitiesForecastSummaryResult);
                        }
                        
                    }

                    @ Override
                    public void failure(Error error) {
                        LogUtil.d(LOG_TAG_STRING, "requestForecastSummary failure");
                        LogUtil.d(LOG_TAG_STRING, StringUtil.toJson(error));
                        // Do nothing when the . And throw the all summary result to Activity.
                        myCitiesForecastSummaryResult.forecastSummaryResult = null;
                        callback.failure(error);
                    }
                });
    }
    public static List<MyCitiesForecastSummaryResult> getAllMyCitiesForecastSummary() {
        List<MyCitiesForecastSummaryResult> resList = new ArrayList <MyCitiesForecastSummaryResult>();
        List <WeatherLocation> allWeatherLocation = WeatherLocationDao.getAllLoaction();
        for (int index = 0; index < allWeatherLocation.size(); index ++) {
            WeatherLocation tempLocation = allWeatherLocation.get(index);
            MyCitiesForecastSummaryResult myCitiesLocation= new MyCitiesForecastSummaryResult();
            myCitiesLocation.forecastSummaryResult = ForecastSummaryResultCacheDao.findForecastSummaryResult(tempLocation.areaId);
            myCitiesLocation.weatherLocation = tempLocation;
            resList.add(myCitiesLocation);
        }
        return resList;
    }
    /**
     * Delete one city in activity just set the WeatherLocation isDeleted value to 1 but not delete record in database.
     * 
     * Delete city and related weather information using this method
     * 
     * @param cityNameCN
     *            - City name CN just get from UI
     * @return
     */
    public static void deleteMyCitiesForecastSummary(String cityNameCN) {
        WeatherLocation weatherLocation = new WeatherLocation();
        weatherLocation = WeatherLocationDao.getLoactionByNameCN(cityNameCN);
        WeatherLocationDao.deleteLocaitonById(weatherLocation.areaId);
    }
    
    public static MyCitiesForecastSummaryResult getDefaultCityForecastSummaryResult() {
        // TODO: Add init data
        return new MyCitiesForecastSummaryResult();
    }
}
