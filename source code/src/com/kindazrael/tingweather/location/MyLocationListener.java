
package com.kindazrael.tingweather.location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.kindazrael.tingweather.common.Callback;
import com.kindazrael.tingweather.model.WeatherLocation;
import com.kindazrael.tingweather.util.LogUtil;

public class MyLocationListener implements BDLocationListener {

    private static String TAG = MyLocationListener.class.getSimpleName();
    private static WeatherLocation weatherLocation = null;
    private Callback <WeatherLocation, ?> callback = null;
    private static LocationClient mLocationClient = null;
    
    private static final Integer TIMES = 3;
    private Integer count = 0;
    
    @ Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null) {
            return;
        }
        count++;
        LogUtil.i(TAG, "第"+count+"次获取地理位置!");
        
        mLocationClient.start();
        String city = location.getCity();
        String province = location.getProvince();
        String district = location.getDistrict();
        LogUtil.i(TAG, "********City=>" + city);
        LogUtil.i(TAG, "********Province=>" + province);
        LogUtil.i(TAG, "********District=>" + district);
        if(city != null || count >=TIMES){
            LocationManager.stopLocationClient(mLocationClient);
            LogUtil.i(TAG, "Stop Location Listener!");
        }
        weatherLocation = new WeatherLocation();
        weatherLocation.city = city;
        weatherLocation.province = province;
        weatherLocation.district = district;

        callback.success(weatherLocation);
    }

    public void setCallback(Callback <WeatherLocation, ?> callback) {
        this.callback = callback;
    }

    public void setLocationClient(LocationClient client) {
        mLocationClient = client;
    }
}
