
package com.kindazrael.tingweather.smartweather;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import android.util.Base64;

import com.kindazrael.tingweather.Constants;
import com.kindazrael.tingweather.common.Callback;
import com.kindazrael.tingweather.common.Error;
import com.kindazrael.tingweather.manager.WeatherService;
import com.kindazrael.tingweather.model.ForecastRealTimeResult;
import com.kindazrael.tingweather.model.ForecastSummaryResult;
import com.kindazrael.tingweather.util.DateUtil;
import com.kindazrael.tingweather.util.LogUtil;

public class SmartWeatherService implements WeatherService {

    private static final String APP_ID_STRING = "96453e4d17087eb3";
    private static final String APP_ID_SIX_PREFIX_STRING = APP_ID_STRING.substring(0, 6);
    private static final String PRIVATE_KEY_STRING = "286785_SmartWeatherAPI_ad81728";
    private static final String PUBLIC_KEY_TEMPALTE_STRING = "http://open.weather.com.cn/data/?areaid={areaid}&type={type}&date={date}&appid="
            + APP_ID_STRING;
    private static final String SMART_WEATHER_ENDPOINT_STRING = "http://open.weather.com.cn";

    private static final String FORECAST_STRING = "forecast";
    private static final String DAY_STRING = "d";
    private static final String OBSERVE_STRING = "observe";

    private static final String LOCATION_STRING = "{areaid}";
    private static final String FORECAST_TYPE_STRING = "{type}";
    private static final String DATE_STRING = "{date}";

    private static final String SMART_WEATHER_API_ALGORITHM = "HmacSHA1";

    private static final String LOG_TAG_STRING = SmartWeatherService.class.getName();

    private SmartWeatherAPI smartWeatherAPI;

    private interface SmartWeatherAPI {

        @ GET ("/data/")
        public void getForecast(@ Query ("areaid") String location, @ Query ("type") String type,
                @ Query ("date") String date, @ Query ("appid") String appid6prefix, @ Query ("key") String key,
                retrofit.Callback <SmartWeatherForecastSummaryResult> callback);

        @ GET ("/data/")
        public void getObserve(@ Query ("areaid") String location, @ Query ("type") String type,
                @ Query ("date") String date, @ Query ("appid") String appid6prefix, @ Query ("key") String key,
                retrofit.Callback <SmartWeatherObserveSummaryResult> callback);

    }

    private class SmartWeatherErrorHandler implements ErrorHandler {

        @ Override
        public Throwable handleError(RetrofitError cause) {
            return cause;
        }
    }

    @ Override
    public void getForecast(String location, int days, final Callback <ForecastSummaryResult, Error> callback) {
        String date = DateUtil.getSmartAPICurrentDateString();
        String forecastType = this.getSmartWeathreForecastType(days);
        String key = this.getKey(location, forecastType, date);

        SmartWeatherAPI smartWeatherService = this.getSmartWeatherService();
        smartWeatherService.getForecast(location, forecastType, date, APP_ID_SIX_PREFIX_STRING, key,
                new retrofit.Callback <SmartWeatherForecastSummaryResult>() {

                    @ Override
                    public void failure(RetrofitError e) {
                        callback.failure(new Error(e));
                    }

                    @ Override
                    public void success(SmartWeatherForecastSummaryResult result, Response resp) {
                        callback.success(SmartWeatherInterfaceMapper.map2ForecastSummaryResult(result));
                    }
                });
    }

    @ Override
    public void getRealTimeWeather(String location, final Callback <ForecastRealTimeResult, Error> callback) {
        String date = DateUtil.getSmartAPICurrentDateString();
        String key = this.getKey(location, OBSERVE_STRING, date);

        SmartWeatherAPI smartWeatherService = this.getSmartWeatherService();
        smartWeatherService.getObserve(location, OBSERVE_STRING, date, APP_ID_SIX_PREFIX_STRING, key,
                new retrofit.Callback <SmartWeatherObserveSummaryResult>() {

                    @ Override
                    public void failure(RetrofitError e) {
                        callback.failure(new Error(e));
                    }

                    @ Override
                    public void success(SmartWeatherObserveSummaryResult result, Response resp) {
                        callback.success(SmartWeatherInterfaceMapper.map2ForecastRealTimeResult(result));
                    }
                });
    }

    private SmartWeatherAPI getSmartWeatherService() {
        if (this.smartWeatherAPI == null) {
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(SMART_WEATHER_ENDPOINT_STRING)
                    .setErrorHandler(new SmartWeatherErrorHandler()).build();
            if (Constants.IS_DEBUG) {
                restAdapter.setLogLevel(LogLevel.FULL);
            }
            this.smartWeatherAPI = restAdapter.create(SmartWeatherAPI.class);
        }

        return this.smartWeatherAPI;
    }

    private String getSmartWeathreForecastType(int days) {
        return FORECAST_STRING + days + DAY_STRING;
    }

    private String getKey(String location, String forecastType, String date) {
        String publicKey = this.getPublicKey(location, forecastType, date);
        return this.sha1HashString(publicKey, PRIVATE_KEY_STRING);
    }

    private String getPublicKey(String location, String forecastType, String date) {
        return PUBLIC_KEY_TEMPALTE_STRING.replace(LOCATION_STRING, location)
                .replace(FORECAST_TYPE_STRING, forecastType).replace(DATE_STRING, date);
    }

    private String sha1HashString(String publicKey, String privateKey) {
        SecretKey secretKey = new SecretKeySpec(privateKey.getBytes(), SMART_WEATHER_API_ALGORITHM);
        Mac mac;
        String resultString = "";
        try {
            mac = Mac.getInstance(SMART_WEATHER_API_ALGORITHM);
            mac.init(secretKey);
            resultString = Base64.encodeToString(mac.doFinal(publicKey.getBytes()), Base64.DEFAULT).trim();
        } catch (NoSuchAlgorithmException e) {
            LogUtil.e(LOG_TAG_STRING, e.getLocalizedMessage(), e);
        } catch (InvalidKeyException e) {
            LogUtil.e(LOG_TAG_STRING, e.getLocalizedMessage(), e);
        }

        return resultString;
    }
}
