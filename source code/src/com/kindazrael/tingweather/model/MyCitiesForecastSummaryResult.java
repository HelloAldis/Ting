package com.kindazrael.tingweather.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kindazrael.tingweather.util.LogUtil;


public class MyCitiesForecastSummaryResult {
    
    public ForecastSummaryResult forecastSummaryResult;
    
    public WeatherLocation weatherLocation;
    
    public static MyCitiesForecastSummaryResult parseJson(String json) {
        MyCitiesForecastSummaryResult result = null;

        if (json != null) {
            Gson gson = new Gson();

            try {
                result = gson.fromJson(json, MyCitiesForecastSummaryResult.class);
            } catch (JsonSyntaxException e) {
                LogUtil.e("MyCitiesForecastSummaryResult",
                        "Failed to parseJson data <\r\n" + json + ">");
            }
        }

        return result;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
