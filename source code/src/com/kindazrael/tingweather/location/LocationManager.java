
package com.kindazrael.tingweather.location;

import java.util.List;
import java.util.Map;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.kindazrael.tingweather.IWApplication;
import com.kindazrael.tingweather.common.Callback;
import com.kindazrael.tingweather.common.Error;
import com.kindazrael.tingweather.dao.CityDao;
import com.kindazrael.tingweather.dao.WeatherLocationDao;
import com.kindazrael.tingweather.model.WeatherLocation;
import com.kindazrael.tingweather.util.LogUtil;

public class LocationManager {

    public static final int LOCATION_SERVICE_NOT_START_ERROR_ID = 20000;
    public static final int NO_LOCATION_LISTENER_ERROR_ID = 20001;
    public static final int LOCATION_REQUEST_GAP_ERROR_ID = 20002;
    public static final String LOCATION_SERVICE_NOT_START_ERROR_MSG = "Location service is not started!";
    public static final String NO_LOCATION_LISTENER_ERROR_MSG = "No location listenner set!";
    public static final String LOCATION_REQUEST_GAP_ERROR_MSG = "Location request gap is too short!";

    private static LocationClient mLocationClient = null;
    private static MyLocationListener myListener = new MyLocationListener();

    public static void requestWeatherLocation(final Callback <WeatherLocation, Error> callback) {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(IWApplication.getInstance()
                    .getApplicationContext()); // 声明LocationClient类
            mLocationClient.setDebug(true);
        }
        myListener.setCallback(callback);
        myListener.setLocationClient(mLocationClient);
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Battery_Saving);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        int result = mLocationClient.requestLocation();

        if (result == 0) {
            System.out.println("Request send!");
        } else if (result == 1) {
            System.out.println("Location service is not started!");
            callback.failure(new Error(new Exception(), LOCATION_SERVICE_NOT_START_ERROR_ID,
                    LOCATION_SERVICE_NOT_START_ERROR_MSG));
        } else if (result == 2) {
            System.out.println("No location listenner set!");
            callback.failure(new Error(new Exception(), NO_LOCATION_LISTENER_ERROR_ID,
                    NO_LOCATION_LISTENER_ERROR_MSG));
        } else if (result == 6) {
            System.out.println("Location request gap is too short!");
            callback.failure(new Error(new Exception(), LOCATION_REQUEST_GAP_ERROR_ID,
                    LOCATION_REQUEST_GAP_ERROR_MSG));
        }
    }
    
    /**
     * Save location
     * @param location
     */
    public static void saveLocation(WeatherLocation location) {
        try {
            WeatherLocation weatherLocation = new WeatherLocation();
            weatherLocation.province = location.province;
            weatherLocation.city = location.city == null ? null : location.city.substring(0,
                    location.city.length() - 1);
            weatherLocation.district = location.district;

            List <Map <String, String>> cityList = CityDao
                    .getCityInfoByCityNameForuzzy(weatherLocation.city);
            if (cityList != null && cityList.size() > 0) {
                weatherLocation.areaId = cityList.get(0).get(
                        CityDao.ASSETS_CITY_TABLE_COLUMN_LOCATIONID);
                LogUtil.i("LocaitonManager", weatherLocation.areaId);
                if(null != weatherLocation.areaId && WeatherLocationDao.checkCity(weatherLocation.areaId)){
                    WeatherLocationDao.insertLocation(weatherLocation);
                }
            }
        } catch (Exception e) {
            LogUtil.e("LocationManager", e.getMessage());
        }
    }
    
    /**
     * Stop LocationClient
     * @param mLocationClient
     */
    public static void stopLocationClient(LocationClient mLocationClient){
        if(mLocationClient != null){
            mLocationClient.unRegisterLocationListener(myListener);
            mLocationClient.stop();
        }
    }
}
