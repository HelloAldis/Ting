
package com.kindazrael.tingweather.manager;

import com.kindazrael.tingweather.common.Callback;
import com.kindazrael.tingweather.common.Error;
import com.kindazrael.tingweather.model.ForecastRealTimeResult;
import com.kindazrael.tingweather.model.ForecastSummaryResult;

public interface WeatherService {

    public void getForecast(String location, int days,
 final Callback <ForecastSummaryResult, Error> callback);

    public void getRealTimeWeather(String location,
            final Callback <ForecastRealTimeResult, Error> callback);
}
