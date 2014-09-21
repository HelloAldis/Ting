package com.kindazrael.tingweather.smartweather;

import java.util.Date;

import com.kindazrael.tingweather.model.ForecastDayResult;
import com.kindazrael.tingweather.model.ForecastRealTimeResult;
import com.kindazrael.tingweather.model.ForecastSummaryResult;
import com.kindazrael.tingweather.model.WeatherType;
import com.kindazrael.tingweather.util.DateUtil;


public class SmartWeatherInterfaceMapper {

    public static ForecastDayResult map2ForecastDayResult(SmartWeatherForecastDayResult smartWeatherForecastDayResult) {
        ForecastDayResult forecastDayResult = new ForecastDayResult();
        if (smartWeatherForecastDayResult != null) {

            forecastDayResult.temperatureLowest = Double.valueOf(smartWeatherForecastDayResult.getFd());
            
            // day temperature will empty in night
            if (smartWeatherForecastDayResult.getFc() != null
                    && smartWeatherForecastDayResult.getFc().trim().length() > 0) {
                forecastDayResult.temperatureHighest = Double.valueOf(smartWeatherForecastDayResult.getFc());
            } else {
                forecastDayResult.temperatureHighest = forecastDayResult.temperatureLowest;
            }

            

            WeatherType daytimeWeatherType = WeatherType.getWeatherTypeById(smartWeatherForecastDayResult.getFa());
            if (daytimeWeatherType != null) {
                forecastDayResult.weatherType = daytimeWeatherType;
            } else {
                forecastDayResult.weatherType = WeatherType.getWeatherTypeById(smartWeatherForecastDayResult.getFb());
            }
        }

        return forecastDayResult;
    }
    
    public static ForecastSummaryResult map2ForecastSummaryResult(
            SmartWeatherForecastSummaryResult smartWeatherForecastSummaryResult) {
        ForecastSummaryResult forecastSummaryResult = new ForecastSummaryResult();
        if (smartWeatherForecastSummaryResult != null) {

            Date date = null;
            date = DateUtil.getDateFromSmartAPIString(smartWeatherForecastSummaryResult.getF().getF0());

            forecastSummaryResult.forecastOfDay1 = SmartWeatherInterfaceMapper
                    .map2ForecastDayResult(smartWeatherForecastSummaryResult.getF().getF1().get(0));
            forecastSummaryResult.forecastOfDay1.date = date;

            forecastSummaryResult.forecastOfDay2 = SmartWeatherInterfaceMapper
                    .map2ForecastDayResult(smartWeatherForecastSummaryResult.getF().getF1().get(1));
            forecastSummaryResult.forecastOfDay2.date = DateUtil.addDays(date, 1);

            forecastSummaryResult.forecastOfDay3 = SmartWeatherInterfaceMapper
                    .map2ForecastDayResult(smartWeatherForecastSummaryResult.getF().getF1().get(2));
            forecastSummaryResult.forecastOfDay3.date = DateUtil.addDays(date, 2);
            ;
        }

        return forecastSummaryResult;
    }

    public static ForecastRealTimeResult map2ForecastRealTimeResult(
            SmartWeatherObserveSummaryResult smartWeatherObserveSummaryResult) {
        ForecastRealTimeResult forecastRealTimeResult = new ForecastRealTimeResult();
        if (smartWeatherObserveSummaryResult != null) {
            forecastRealTimeResult.temperature = Double.valueOf(smartWeatherObserveSummaryResult.getL().getL1());
            forecastRealTimeResult.humidity = Integer.valueOf(smartWeatherObserveSummaryResult.getL().getL2());
            forecastRealTimeResult.reportTimeString = smartWeatherObserveSummaryResult.getL().getL7();
        }

        return forecastRealTimeResult;
    }

    public static ForecastSummaryResult fixMissDayTemperatureInNightIssue(ForecastSummaryResult forecastSummaryResult) {
        // day 1 day temperature will empty in night, if current temperature is
        // higher than lowest temperature, so current temperature is the highest
        // temperature
        if (forecastSummaryResult.realTimeResult.temperature > forecastSummaryResult.forecastOfDay1.temperatureHighest) {
            forecastSummaryResult.forecastOfDay1.temperatureHighest = forecastSummaryResult.realTimeResult.temperature;
        }

        return forecastSummaryResult;
    }
}
