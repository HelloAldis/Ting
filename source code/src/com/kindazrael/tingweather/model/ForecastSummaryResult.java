
package com.kindazrael.tingweather.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kindazrael.tingweather.util.LogUtil;

public class ForecastSummaryResult {

    public ForecastRealTimeResult realTimeResult;

    public ForecastDayResult forecastOfDay1;
    public ForecastDayResult forecastOfDay2;
    public ForecastDayResult forecastOfDay3;

    public static ForecastSummaryResult parseJson(String json) {
        ForecastSummaryResult result = null;

        if (json != null) {
            Gson gson = new Gson();

            try {
                result = gson.fromJson(json, ForecastSummaryResult.class);
            } catch (JsonSyntaxException e) {
                LogUtil.e("ForecastSummaryResult",
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
