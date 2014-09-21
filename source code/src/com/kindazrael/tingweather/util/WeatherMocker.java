package com.kindazrael.tingweather.util;

import java.util.ArrayList;
import java.util.List;

import com.kindazrael.tingweather.model.WeatherType;

public class WeatherMocker {

    private static final List <WeatherType> weatherList = new ArrayList <WeatherType>();
    static {
        weatherList.add(WeatherType.Sunny);
        weatherList.add(WeatherType.Cloudy);
        weatherList.add(WeatherType.Overcast);

        weatherList.add(WeatherType.Shower);
		// weatherList.add(WeatherType.Thundershower);

        weatherList.add(WeatherType.LightRain);
        // weatherList.add(WeatherType.LightToModerateRain);
        weatherList.add(WeatherType.ModerateRain);
        // weatherList.add(WeatherType.ModerateToHeavyRain);
        weatherList.add(WeatherType.HeavyRain);
        // weatherList.add(WeatherType.HeavyRainToStorm);
        // weatherList.add(WeatherType.Storm);
        // weatherList.add(WeatherType.StormToHeavyStorm);
        // weatherList.add(WeatherType.HeavyStorm);
        // weatherList.add(WeatherType.HeavyToSevereStorm);
        // weatherList.add(WeatherType.SevereStorm);

        weatherList.add(WeatherType.Sleet);

        weatherList.add(WeatherType.SnowFlurry);
        weatherList.add(WeatherType.LightSnow);
        // weatherList.add(WeatherType.LightToModerateSnow);
        weatherList.add(WeatherType.ModerateSnow);
        // weatherList.add(WeatherType.ModerateToHeavySnow);
        weatherList.add(WeatherType.HeavySnow);
        // weatherList.add(WeatherType.HeavySnowToSnowstorm);
        // weatherList.add(WeatherType.Snowstorm);

        weatherList.add(WeatherType.Haze);
        weatherList.add(WeatherType.Foggy);
        weatherList.add(WeatherType.Unknown);
    }

    private static int index = 0;
    
    private static boolean open = true;
    
    public static WeatherType getMockWeather(WeatherType realWeather) {
        if (open) {
            WeatherType mockWeather = weatherList.get((index % weatherList.size()));
            index++;
            return mockWeather;
        } else {
            return realWeather;
        }
    }
}
