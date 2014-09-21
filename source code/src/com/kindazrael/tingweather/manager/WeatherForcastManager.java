
package com.kindazrael.tingweather.manager;

import com.kindazrael.tingweather.common.Callback;
import com.kindazrael.tingweather.common.Error;
import com.kindazrael.tingweather.dao.ForecastSummaryResultCacheDao;
import com.kindazrael.tingweather.model.ForecastRealTimeResult;
import com.kindazrael.tingweather.model.ForecastSummaryResult;
import com.kindazrael.tingweather.model.WeatherLocation;
import com.kindazrael.tingweather.smartweather.SmartWeatherInterfaceMapper;
import com.kindazrael.tingweather.smartweather.SmartWeatherService;
import com.kindazrael.tingweather.util.LogUtil;
import com.kindazrael.tingweather.util.StringUtil;

public class WeatherForcastManager {

    public static final int NO_DATA_ERROR_ID = 10000;
    public static final String NO_DATA_ERROR_MSG = "No data!";

    private static final String LOG_TAG_STRING = WeatherForcastManager.class.getName();
    private static final SmartWeatherService SMART_WEATHER_MANAGER = new SmartWeatherService();

    public static void requestForecastSummary(final WeatherLocation l, final Callback <ForecastSummaryResult, Error> callback) {
        if (l == null || callback == null) {
            throw new IllegalArgumentException("Location and callback must not be NULL.");
        }

        final ForecastSummaryResult forecastSummaryResult = new ForecastSummaryResult();
        final APIStatus status = new APIStatus();
        
        SMART_WEATHER_MANAGER.getForecast(l.areaId, 3, new Callback <ForecastSummaryResult, Error>() {

            @ Override
            public void success(ForecastSummaryResult result) {
                forecastSummaryResult.forecastOfDay1 = result.forecastOfDay1;
                forecastSummaryResult.forecastOfDay2 = result.forecastOfDay2;
                forecastSummaryResult.forecastOfDay3 = result.forecastOfDay3;
                
                synchronized (status) {
                    LogUtil.d(LOG_TAG_STRING, "getForecast success");

                    if (status.hasError) {
                        //Do nothing because already has error;
                    } else {
                        if (status.isFinishedAll()) {
                            // if finished all with success
                            LogUtil.d(LOG_TAG_STRING, "all success");
                            SmartWeatherInterfaceMapper.fixMissDayTemperatureInNightIssue(forecastSummaryResult);
                            callback.success(forecastSummaryResult);
                            ForecastSummaryResultCacheDao.saveForecastSummaryResult(l.areaId, forecastSummaryResult);
                        } else {
                            // if other some api not finished
                            LogUtil.d(LOG_TAG_STRING, "some not finished");
                            status.processSuccess();
                        }
                    }
                }
            }

            @ Override
            public void failure(Error error) {
                ForecastSummaryResult forecastSummaryResultFromDB = ForecastSummaryResultCacheDao
                        .findForecastSummaryResult(l.areaId);
                
                synchronized (status) {
                    LogUtil.d(LOG_TAG_STRING, "getForecast failure");

                    if (status.hasError) {
                        // Do nothing because already has error;
                    } else {
                        if (forecastSummaryResultFromDB == null) {
                            LogUtil.d(LOG_TAG_STRING, "Both api and db has no data");
                            Error e = new Error(null, NO_DATA_ERROR_ID, NO_DATA_ERROR_MSG);
                            callback.failure(e);
                        } else {
                            LogUtil.d(LOG_TAG_STRING, "Api no data return db data");
                            callback.success(forecastSummaryResultFromDB);
                        }
                    }

                    status.processFailure();
                }
            }
        });
        
        SMART_WEATHER_MANAGER.getRealTimeWeather(l.areaId, new Callback <ForecastRealTimeResult, Error>() {

            @ Override
            public void success(ForecastRealTimeResult result) {
                forecastSummaryResult.realTimeResult = result;

                synchronized (status) {
                    LogUtil.d(LOG_TAG_STRING, "getRealTimeWeather success");

                    if (status.hasError) {
                        // Do nothing because already has error;
                    } else {
                        if (status.isFinishedAll()) {
                            // if finished all with success
                            LogUtil.d(LOG_TAG_STRING, "all success");
                            SmartWeatherInterfaceMapper.fixMissDayTemperatureInNightIssue(forecastSummaryResult);
                            callback.success(forecastSummaryResult);
                            ForecastSummaryResultCacheDao.saveForecastSummaryResult(l.areaId, forecastSummaryResult);
                        } else {
                            // if other some api not finished
                            LogUtil.d(LOG_TAG_STRING, "some not finished");
                            status.processSuccess();
                        }
                    }
                }
            }

            @ Override
            public void failure(Error error) {
                ForecastSummaryResult forecastSummaryResultFromDB = ForecastSummaryResultCacheDao
                        .findForecastSummaryResult(l.areaId);

                synchronized (status) {
                    LogUtil.d(LOG_TAG_STRING, "getRealTimeWeather failure");

                    if (status.hasError) {
                        // Do nothing because already has error;
                    } else {
                        if (forecastSummaryResultFromDB == null) {
                            LogUtil.d(LOG_TAG_STRING, "Both api and db has no data");
                            Error e = new Error(null, NO_DATA_ERROR_ID, NO_DATA_ERROR_MSG);
                            callback.failure(e);
                        } else {
                            LogUtil.d(LOG_TAG_STRING, "Api no data return db data");
                            callback.success(forecastSummaryResultFromDB);
                        }
                    }

                    status.processFailure();
                }

            }
        });
    }
    
    protected static class APIStatus {

        protected static final int TOTAL_API_COUNT = 2;

        protected int finishedCount = 0;
        protected boolean hasError = false;

        protected void processSuccess() {
            this.finishedCount++;
        }

        protected void processFailure() {
            this.hasError = true;
        }

        protected boolean isFinishedAll() {
            return this.finishedCount == (TOTAL_API_COUNT - 1);
        }
    }
    

    public static void test() {
        // Test smart weather
        WeatherLocation l = new WeatherLocation();
        l.areaId = "101200101";
        WeatherForcastManager.requestForecastSummary(l, new Callback <ForecastSummaryResult, Error>() {
            
            @ Override
            public void success(ForecastSummaryResult result) {
                LogUtil.d(LOG_TAG_STRING, "requestForecastSummary success");
                LogUtil.d(LOG_TAG_STRING, StringUtil.toJson(result));
            }
            
            @ Override
            public void failure(Error error) {
                LogUtil.d(LOG_TAG_STRING, "requestForecastSummary failure");
                LogUtil.d(LOG_TAG_STRING, StringUtil.toJson(error));
            }
        });
        
        
    }
}
