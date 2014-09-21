
package com.kindazrael.tingweather.smartweather;

import java.util.List;

public class SmartWeatherForecastDaysResult {

    private String f0;
    private List <SmartWeatherForecastDayResult> f1;

    public String getF0() {
        return f0;
    }

    public void setF0(String f0) {
        this.f0 = f0;
    }

    public List <SmartWeatherForecastDayResult> getF1() {
        return f1;
    }

    public void setF1(List <SmartWeatherForecastDayResult> f1) {
        this.f1 = f1;
    }
}
