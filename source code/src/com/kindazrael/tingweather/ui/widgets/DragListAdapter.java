
package com.kindazrael.tingweather.ui.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kindazrael.tingweather.R;
import com.kindazrael.tingweather.dao.WeatherLocationDao;
import com.kindazrael.tingweather.model.MyCitiesForecastSummaryResult;
import com.kindazrael.tingweather.model.WeatherLocation;
import com.kindazrael.tingweather.model.WeatherType;

/***
 * 
 * 
 * 
 */

class Weather{
    public WeatherType weatherType;
    public Integer temperatureHighest;
    public Integer temperatureLowest; 
    
}
public class DragListAdapter extends BaseAdapter {

    private static final String TAG = "DragListAdapter";
    // private ArrayList <String> arrayTitles;

    public List <MyCitiesForecastSummaryResult> locationInfoList;
    public List <WeatherLocation> locationList;
    private final Context context;
    public boolean isHidden;
    
    private final Map<String, Weather> weatherMap;

    public DragListAdapter (Context context, List <MyCitiesForecastSummaryResult> locationInfoList, List <WeatherLocation> locationList) {
        this.locationInfoList = locationInfoList;
        this.context = context;
        this.locationList = locationList;
        
        weatherMap = new HashMap<String, Weather>();
        for (MyCitiesForecastSummaryResult result : locationInfoList) {
            if (result != null && result.forecastSummaryResult != null
                    && result.forecastSummaryResult.forecastOfDay1 != null) {
                Weather weather = new Weather();
                weather.weatherType = result.forecastSummaryResult.forecastOfDay1.weatherType;
                weather.temperatureLowest = (int) result.forecastSummaryResult.forecastOfDay1.temperatureLowest;
                weather.temperatureHighest = (int) result.forecastSummaryResult.forecastOfDay1.temperatureHighest;
                weatherMap.put(result.weatherLocation.areaId, weather);
            }
        }
    }

    public void showDropItem(boolean showItem) {
        this.ShowItem = showItem;
    }

    public void setInvisiblePosition(int position) {
        invisilePosition = position;
    }

    @ Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.drag_list_item, null);
        TextView tvCityName = (TextView) convertView.findViewById(R.id.drag_list_item_text);
        TextView tvCityWeather = (TextView) convertView.findViewById(R.id.tv_weather);
        ImageView ivCityWeather = (ImageView) convertView.findViewById(R.id.iv_weather);
        
        tvCityName.setText(locationList.get(position).city);
        Weather weather = weatherMap.get(locationList.get(position).areaId);
        if(weather != null){
            tvCityWeather.setText(weather.temperatureLowest+"/"+weather.temperatureHighest + "℃   ");
            ivCityWeather.setBackgroundResource(weather.weatherType.getWeatherDescriptor().getIconForCityList());
        }
        
        if (isChanged) {
            if (position == invisilePosition) {
                if (!ShowItem) {
                    convertView.findViewById(R.id.drag_list_item_text)
                            .setVisibility(View.INVISIBLE);
                    convertView.findViewById(R.id.drag_list_item_image).setVisibility(
                            View.INVISIBLE);
                    convertView.findViewById(R.id.drag_list_item_temp)
                            .setVisibility(View.INVISIBLE);
                }
            }
            if (lastFlag != -1) {
                if (lastFlag == 1) {
                    if (position > invisilePosition) {
                        Animation animation;
                        animation = getFromSelfAnimation(0, -height);
                        convertView.startAnimation(animation);
                    }
                } else if (lastFlag == 0) {
                    if (position < invisilePosition) {
                        Animation animation;
                        animation = getFromSelfAnimation(0, height);
                        convertView.startAnimation(animation);
                    }
                }
            }
        }
        return convertView;
    }

    private int invisilePosition = -1;
    private boolean isChanged = true;
    private boolean ShowItem = false;

    public void exchange(int startPosition, int endPosition) {
        Object startObject = getItem(startPosition);
        if (startPosition < endPosition) {
            locationList.add(endPosition + 1, (WeatherLocation) startObject);
            locationList.remove(startPosition);
        } else {
            locationList.add(endPosition, (WeatherLocation) startObject);
            locationList.remove(startPosition + 1);
        }
        isChanged = true;
    }

    public void exchangeCopy(int startPosition, int endPosition) {
        WeatherLocation startObject = (WeatherLocation) getCopyItem(startPosition);
        WeatherLocation endObject = (WeatherLocation) getCopyItem(endPosition);

        int sort = startObject.sortOrder;
        startObject.sortOrder = endObject.sortOrder;
        endObject.sortOrder = sort;

        WeatherLocationDao.updateLocation(startObject);
        WeatherLocationDao.updateLocation(endObject);
        if (startPosition < endPosition) {
            mCopyList.add(endPosition + 1, startObject);
            mCopyList.remove(startPosition);

        } else {
            mCopyList.add(endPosition, startObject);
            mCopyList.remove(startPosition + 1);
        }
        isChanged = true;
    }
    
    public void delete(int postion){
        copyList();
        WeatherLocationDao.deleteLocaitonById(mCopyList.get(postion).areaId);
        mCopyList.remove(postion);
        locationList.remove(postion);
    }

    public Object getCopyItem(int position) {
        return mCopyList.get(position);
    }

    @ Override
    public int getCount() {
        return locationList.size();
    }

    @ Override
    public Object getItem(int position) {
        return locationList.get(position);
    }

    @ Override
    public long getItemId(int position) {
        return position;
    }

    public void addDragItem(int start, Object obj) {
        Log.i(TAG, "start" + start);
        locationList.remove(start);
        locationList.add(start, (WeatherLocation) obj);
    }

    private final List <WeatherLocation> mCopyList = new ArrayList <WeatherLocation>();

    public void copyList() {
        mCopyList.clear();
        for (WeatherLocation location : locationList) {
            mCopyList.add(location);
        }
    }

    public void pastList() {
        locationList.clear();
        for (WeatherLocation location : mCopyList) {
            locationList.add(location);
        }
    }

    private int lastFlag = -1;
    private int height;

    public void setLastFlag(int flag) {
        lastFlag = flag;
    }

    public void setHeight(int value) {
        height = value;
    }

    public Animation getFromSelfAnimation(int x, int y) {
        TranslateAnimation go = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.ABSOLUTE, x, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, y);
        go.setInterpolator(new AccelerateDecelerateInterpolator());
        go.setFillAfter(true);
        go.setDuration(100);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }

    public Animation getToSelfAnimation(int x, int y) {
        TranslateAnimation go = new TranslateAnimation(Animation.ABSOLUTE, x,
                Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, y, Animation.RELATIVE_TO_SELF, 0);
        go.setInterpolator(new AccelerateDecelerateInterpolator());
        go.setFillAfter(true);
        go.setDuration(100);
        go.setInterpolator(new AccelerateInterpolator());
        return go;
    }
}
