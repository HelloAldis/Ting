
package com.kindazrael.tingweather.smartweather;

public class SmartWeatherForecastSummaryResult {

    private SmartWeatherForecastCityResult c;
    private SmartWeatherForecastDaysResult f;

    public SmartWeatherForecastCityResult getC() {
        return c;
    }

    public void setC(SmartWeatherForecastCityResult c) {
        this.c = c;
    }

    public SmartWeatherForecastDaysResult getF() {
        return f;
    }

    public void setF(SmartWeatherForecastDaysResult f) {
        this.f = f;
    }
}
