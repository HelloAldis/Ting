
package com.kindazrael.tingweather.model;

import java.util.HashMap;
import java.util.Map;

import com.kindazrael.tingweather.model.descriptor.CloudyDescriptor;
import com.kindazrael.tingweather.model.descriptor.FoggyDescriptor;
import com.kindazrael.tingweather.model.descriptor.HazeDescriptor;
import com.kindazrael.tingweather.model.descriptor.HeavyRainDescriptor;
import com.kindazrael.tingweather.model.descriptor.HeavyRainToStormDescriptor;
import com.kindazrael.tingweather.model.descriptor.HeavySnowDescriptor;
import com.kindazrael.tingweather.model.descriptor.HeavySnowToSnowstormDescriptor;
import com.kindazrael.tingweather.model.descriptor.HeavyStormDescriptor;
import com.kindazrael.tingweather.model.descriptor.HeavyToSevereStormDescriptor;
import com.kindazrael.tingweather.model.descriptor.LightRainDescriptor;
import com.kindazrael.tingweather.model.descriptor.LightSnowDescriptor;
import com.kindazrael.tingweather.model.descriptor.LightToModerateRainDescriptor;
import com.kindazrael.tingweather.model.descriptor.LightToModerateSnowDescriptor;
import com.kindazrael.tingweather.model.descriptor.ModeratToHeavyRainDescriptor;
import com.kindazrael.tingweather.model.descriptor.ModerateRainDescriptor;
import com.kindazrael.tingweather.model.descriptor.ModerateSnowDescriptor;
import com.kindazrael.tingweather.model.descriptor.ModerateToHeavySnowDescriptor;
import com.kindazrael.tingweather.model.descriptor.OvercastDescriptor;
import com.kindazrael.tingweather.model.descriptor.SevereStormDescriptor;
import com.kindazrael.tingweather.model.descriptor.ShowerDescriptor;
import com.kindazrael.tingweather.model.descriptor.SleetDescriptor;
import com.kindazrael.tingweather.model.descriptor.SnowFlurryDescriptor;
import com.kindazrael.tingweather.model.descriptor.SnowstormDescriptor;
import com.kindazrael.tingweather.model.descriptor.StormDescriptor;
import com.kindazrael.tingweather.model.descriptor.StormToHeavyStormDescriptor;
import com.kindazrael.tingweather.model.descriptor.SunnyDescriptor;
import com.kindazrael.tingweather.model.descriptor.ThundershowerDescriptor;
import com.kindazrael.tingweather.model.descriptor.UnknownDescriptor;
import com.kindazrael.tingweather.model.descriptor.WeatherTypeDescriptor;

public class WeatherType {

    private static final Map <String, WeatherType> mapping = new HashMap <String, WeatherType>();
    public static final Map <String, WeatherTypeDescriptor> descriptorMapping = new HashMap <String, WeatherTypeDescriptor>();

    public String ID;
    public String description;

    public static final WeatherType Sunny = new WeatherType("00", "晴", new SunnyDescriptor());
    public static final WeatherType Cloudy = new WeatherType("01", "多云", new CloudyDescriptor());
    public static final WeatherType Overcast = new WeatherType("02", "阴", new OvercastDescriptor());
    public static final WeatherType Shower = new WeatherType("03", "阵雨", new ShowerDescriptor());
    public static final WeatherType Thundershower = new WeatherType("04", "雷阵雨", new ThundershowerDescriptor());
    public static final WeatherType Sleet = new WeatherType("06", "雨夹雪", new SleetDescriptor());

    public static final WeatherType LightRain = new WeatherType("07", "小雨", new LightRainDescriptor());
    public static final WeatherType ModerateRain = new WeatherType("08", "中雨", new ModerateRainDescriptor());
    public static final WeatherType HeavyRain = new WeatherType("09", "大雨", new HeavyRainDescriptor());
    public static final WeatherType Storm = new WeatherType("10", "暴雨", new StormDescriptor());
    public static final WeatherType HeavyStorm = new WeatherType("11", "大暴雨", new HeavyStormDescriptor());
    public static final WeatherType SevereStorm = new WeatherType("12", "特大暴雨", new SevereStormDescriptor());

    public static final WeatherType SnowFlurry = new WeatherType("13", "阵雪", new SnowFlurryDescriptor());
    public static final WeatherType LightSnow = new WeatherType("14", "小雪", new LightSnowDescriptor());
    public static final WeatherType ModerateSnow = new WeatherType("15", "中雪", new ModerateSnowDescriptor());
    public static final WeatherType HeavySnow = new WeatherType("16", "大雪", new HeavySnowDescriptor());
    public static final WeatherType Snowstorm = new WeatherType("17", "暴雪", new SnowstormDescriptor());

    public static final WeatherType Foggy = new WeatherType("18", "雾", new FoggyDescriptor());

    public static final WeatherType LightToModerateRain = new WeatherType("21", "小到中雨", new LightToModerateRainDescriptor());
    public static final WeatherType ModerateToHeavyRain = new WeatherType("22", "中到大雨 ", new ModeratToHeavyRainDescriptor());
    public static final WeatherType HeavyRainToStorm = new WeatherType("23", "大到暴雨", new HeavyRainToStormDescriptor());
    public static final WeatherType StormToHeavyStorm = new WeatherType("24", "暴雨到大暴雨", new StormToHeavyStormDescriptor());
    public static final WeatherType HeavyToSevereStorm = new WeatherType("25", "大暴雨到特大暴雨", new HeavyToSevereStormDescriptor());
    public static final WeatherType LightToModerateSnow = new WeatherType("26", "小到中雪", new LightToModerateSnowDescriptor());
    public static final WeatherType ModerateToHeavySnow = new WeatherType("27", "中到大雪", new ModerateToHeavySnowDescriptor());
    public static final WeatherType HeavySnowToSnowstorm = new WeatherType("28", "大到暴雪", new HeavySnowToSnowstormDescriptor());

    public static final WeatherType Haze = new WeatherType("53", "霾", new HazeDescriptor());

	public static final WeatherType Unknown = new WeatherType("99", "罕见",
			new UnknownDescriptor());

    private WeatherType (String ID, String description, WeatherTypeDescriptor descriptor) {
        this.ID = ID;
        this.description = description;

        mapping.put(ID, this);
        descriptorMapping.put(ID, descriptor);
    }

    public static WeatherType getWeatherTypeById(String id) {
        return mapping.get(id);
    }
    
    public WeatherTypeDescriptor getWeatherDescriptor() {
        return (null != descriptorMapping.get(this.ID)) ? descriptorMapping.get(this.ID) : descriptorMapping.get("99");
    }
}
