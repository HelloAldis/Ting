
package com.kindazrael.tingweather.ui.activity;

/*
 * Copyright 2014 Augmentum Inc. All rights reserved.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.kindazrael.tingweather.R;
import com.kindazrael.tingweather.base.HomeBaseActivity;
import com.kindazrael.tingweather.common.Callback;
import com.kindazrael.tingweather.common.Error;
import com.kindazrael.tingweather.dao.CityDao;
import com.kindazrael.tingweather.location.LocationManager;
import com.kindazrael.tingweather.manager.MyCitiesForecastManager;
import com.kindazrael.tingweather.model.MyCitiesForecastSummaryResult;
import com.kindazrael.tingweather.model.WeatherLocation;
import com.kindazrael.tingweather.model.WeatherType;
import com.kindazrael.tingweather.ui.widgets.DragListAdapter;
import com.kindazrael.tingweather.ui.widgets.DragListView;
import com.kindazrael.tingweather.util.LogUtil;
import com.kindazrael.tingweather.util.StringUtil;
import com.kindazrael.tingweather.util.WeatherMocker;

public class MainActivity extends HomeBaseActivity {

    private static final String TAG = "MainActivity";
    private ViewPager locationViewPager = null;
    private Button btAddCityInDragList = null;
    private List <MyCitiesForecastSummaryResult> locationInfoList = new ArrayList <MyCitiesForecastSummaryResult>();
    private SlidingMenu menu;

    private final List <WeatherLocation> locationList = new ArrayList <WeatherLocation>();

    // Add city
    private GridView hotCityListGridView = null;
    private String defaultHotCityList[] = null;
    private AutoCompleteTextView searchCityEditText = null;
    private String seachResultCityArrayList[] = new String [] {};
    private ArrayAdapter <String> adapter = null;
    private InputMethodManager imm = null;
    private Button btnClearCityText = null;

    private DragListAdapter adapterMenu = null;

    @ Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 去掉信息栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        setContentView(R.layout.activity_main);

        locationViewPager = (ViewPager) findViewById(R.id.loocation_view_pager);

        menu = new SlidingMenu(this);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        // menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        
        LogUtil.d(TAG, String.format("%d", MyCitiesForecastManager.getAllMyCitiesForecastSummary().size()));
        if (MyCitiesForecastManager.getAllMyCitiesForecastSummary().size() == 0) {
        	initAddCity();
        } else {
        	sortAndDeleteCity();
        }

        menu.setOnOpenListener(new OnOpenListener() {

            @ Override
            public void onOpen() {
                if (MyCitiesForecastManager.getAllMyCitiesForecastSummary().size() == 0) {
                	initAddCity();
                } else {
                	sortAndDeleteCity();
                }
            }
        });

        menu.setOnOpenedListener(new OnOpenedListener() {

            @ Override
            public void onOpened() {

            }
        });

        menu.setOnCloseListener(new OnCloseListener() {

            @ Override
            public void onClose() {
            	if (searchCityEditText != null && MainActivity.this.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) { 
                	//关闭输入法 
                	InputMethodManager imm = (InputMethodManager) getApplicationContext() .getSystemService(Context.INPUT_METHOD_SERVICE); 
                	imm.hideSoftInputFromWindow(searchCityEditText.getWindowToken(), 0);
                }
                initData();                
            }

        });

        menu.setOnClosedListener(new OnClosedListener() {

            @ Override
            public void onClosed() {
                locationViewPager.setAdapter(pagerAdapter);
                pagerAdapter.notifyDataSetChanged();
            }

        });
    }

    LocationInfoPagerAdapter pagerAdapter;

    public void initData() {
        // 数据结果
        getLocationInfoList();
        locationList.clear();
        for (int i = 0; i < locationInfoList.size(); i++) {
            locationList.add(locationInfoList.get(i).weatherLocation);
        }
    }

    private void initAddCity() {
        menu.setMenu(R.layout.menu_frame);

        // Add City
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        hotCityListGridView = (GridView) findViewById(R.id.hot_city_list_grid_view);
        defaultHotCityList = getResources().getStringArray(R.array.hot_city_list);
        btnClearCityText = (Button) findViewById(R.id.clear_button);
        hotCityListGridView.setAdapter(new CityAdapter());
        hotCityListGridView.setOnItemClickListener(onItemClickListener);
        searchCityEditText = (AutoCompleteTextView) findViewById(R.id.search_name_edit_text);
        searchCityEditText.addTextChangedListener(searchWatcher);
        searchCityEditText.setThreshold(1);
        searchCityEditText.setOnItemClickListener(onSearchItemClickListener);
        btnClearCityText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				searchCityEditText.setText("");
			}
		});
    }

    private void sortAndDeleteCity() {
        menu.setMenu(R.layout.sort_city_menu_fram);
        DragListView dragListView = (DragListView) findViewById(R.id.other_drag_list);
        initCityListData(dragListView);
        btAddCityInDragList = (Button) findViewById(R.id.bt_addcity);
        btAddCityInDragList.setOnClickListener(onClickAddCityListener);
    }
    
    public void initCityListData(ListView dragListView){
        initData();
        adapterMenu = new DragListAdapter(this, locationInfoList, locationList);
        dragListView.setAdapter(adapterMenu);
    }

    private void getLocationInfoList() {
        try {
            locationInfoList = MyCitiesForecastManager.getAllMyCitiesForecastSummary();
        } catch (Exception e) {
            LogUtil.e(TAG, "Error message: " + e);
        }
        if (locationInfoList.size() == 0) {
            locationInfoList.add(MyCitiesForecastManager.getDefaultCityForecastSummaryResult());
        }
    }

    @ Override
    protected void onResume() {
        super.onResume();
        getLocationInfoList();
        pagerAdapter = new LocationInfoPagerAdapter(this);
        locationViewPager.setAdapter(pagerAdapter);
    }

    private final OnClickListener onClickAddCityListener = new OnClickListener() {

        @ Override
        public void onClick(View v) {
            initAddCity();
        }
    };

    private final OnClickListener onClickListener = new OnClickListener() {

        @ Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.refresh_weather_button:
                    startRotateAnimation(v);
                    break;
                case R.id.menu_button:
                    menu.showMenu();
                    break;
            }
        }
    };

    public class LocationInfoPagerAdapter extends PagerAdapter {

        private LayoutInflater inflater = null;

        public LocationInfoPagerAdapter (Context context) {
            inflater = LayoutInflater.from(context);
        }

        @ Override
        public int getCount() {
            return (null != locationInfoList) ? locationInfoList.size() : 0;
        }

        @ Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @ Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = inflater.inflate(R.layout.location_detail_info_pager_view, null);
            RelativeLayout rootViewLayout = (RelativeLayout) view
                    .findViewById(R.id.location_info_root_layout);
            RelativeLayout containerlayout = (RelativeLayout) view
                    .findViewById(R.id.content_layout);
            Button btnMenu = (Button) view.findViewById(R.id.menu_button);
            Button btnRefreshWeather = (Button) view.findViewById(R.id.refresh_weather_button);
            TextView tvCityName = (TextView) view.findViewById(R.id.city_name_text_view);
            TextView tvWeatherStatus = (TextView) view.findViewById(R.id.weather_status_text_view);
            TextView tvTempture = (TextView) view.findViewById(R.id.tempture_text_view);

            LinearLayout weatherBottonlayout = (LinearLayout) view
                    .findViewById(R.id.botton_weather_layout);
            TextView todayWeatherStatus = (TextView) view
                    .findViewById(R.id.today_weather_status_text_view);
            TextView tomorrowWeatherStatus = (TextView) view
                    .findViewById(R.id.tomorrow_weather_status_text_view);
            TextView lastDayWeatherStatus = (TextView) view
                    .findViewById(R.id.lastday_weather_status_text_view);

            TextView todayLabel = (TextView) view.findViewById(R.id.today_text_view);
            TextView tomorrowLabel = (TextView) view.findViewById(R.id.tomorrow_text_view);
            TextView lastDayLabel = (TextView) view.findViewById(R.id.lastday_text_view);

            ImageView ivToday = (ImageView) view.findViewById(R.id.today_weather_image_icon);
            ImageView ivTomorrow = (ImageView) view.findViewById(R.id.tomorrow_weather_image_icon);
            ImageView ivLastDay = (ImageView) view.findViewById(R.id.lastday_weather_image_icon);
            
            TextView todayTempDuration = (TextView) view.findViewById(R.id.today_weather_temp_duration_text_view);
            TextView tomorrowTempDuration = (TextView) view.findViewById(R.id.tomorrow_weather_temp_duration_text_view);
            TextView lastDayTempDuration = (TextView) view.findViewById(R.id.lastday_weather_temp_duration_text_view);
            
            MyCitiesForecastSummaryResult locationInfo = locationInfoList.get(position);
            if (null != locationInfo && null != locationInfo.weatherLocation) {
                tvCityName.setText(locationInfo.weatherLocation.city);
            }
            if (null != locationInfo && null != locationInfo.forecastSummaryResult) {
                WeatherType todayWeatherType = locationInfo.forecastSummaryResult.forecastOfDay1.weatherType;
				todayWeatherType = WeatherMocker
						.getMockWeather(todayWeatherType);
                // 获取第一天的天气
                if (null != todayWeatherType) {
                    tvWeatherStatus.setText(todayWeatherType.description);
                    tvTempture.setText(StringUtil
                            .formartTemperature(locationInfo.forecastSummaryResult.realTimeResult.temperature) + "°");
                    todayWeatherStatus.setText(todayWeatherType.description);
                    containerlayout.addView(todayWeatherType.getWeatherDescriptor()
                            .getDisplayingView(MainActivity.this));
                    rootViewLayout.setBackgroundResource(todayWeatherType.getWeatherDescriptor()
                            .getBackground());
                    ivToday.setBackgroundResource(todayWeatherType.getWeatherDescriptor().getIcon());
                    todayTempDuration.setText(StringUtil
                            .formartTemperature(locationInfo.forecastSummaryResult.forecastOfDay1.temperatureLowest) + " / " + StringUtil
                                            .formartTemperature(locationInfo.forecastSummaryResult.forecastOfDay1.temperatureHighest)
                                    + "℃");
                    // 设置样式
                    tvCityName.setTextAppearance(MainActivity.this, todayWeatherType
                            .getWeatherDescriptor().getWeatherLocationStyle());
                    tvWeatherStatus.setTextAppearance(MainActivity.this, todayWeatherType
                            .getWeatherDescriptor().getTemperatureLabelStyle());
                    tvWeatherStatus.setTextAppearance(MainActivity.this, todayWeatherType
                            .getWeatherDescriptor().getWeatherStatusLabelStyle());

                    weatherBottonlayout.setBackgroundColor(getResources().getColor(
                            todayWeatherType.getWeatherDescriptor().getBottonBackground()));
                    todayLabel.setTextAppearance(MainActivity.this, todayWeatherType
                            .getWeatherDescriptor().getBottonWeatherLabelStyle());
                    tomorrowLabel.setTextAppearance(MainActivity.this, todayWeatherType
                            .getWeatherDescriptor().getBottonWeatherLabelStyle());
                    lastDayLabel.setTextAppearance(MainActivity.this, todayWeatherType
                            .getWeatherDescriptor().getBottonWeatherLabelStyle());

                    todayWeatherStatus.setTextAppearance(MainActivity.this, todayWeatherType
                            .getWeatherDescriptor().getBottonWeatherLabelStyle());
                    tomorrowWeatherStatus.setTextAppearance(MainActivity.this, todayWeatherType
                            .getWeatherDescriptor().getBottonWeatherLabelStyle());
                    lastDayWeatherStatus.setTextAppearance(MainActivity.this, todayWeatherType
                            .getWeatherDescriptor().getBottonWeatherLabelStyle());
                }
                // 获取第二天的天气
                WeatherType weatherTomorrowWeatherType = WeatherType
                        .getWeatherTypeById(locationInfo.forecastSummaryResult.forecastOfDay2.weatherType.ID);
                if (null != weatherTomorrowWeatherType) {
                    tomorrowWeatherStatus.setText(weatherTomorrowWeatherType.description);
                    ivTomorrow.setBackgroundResource(weatherTomorrowWeatherType
                            .getWeatherDescriptor().getIcon());
                    tomorrowTempDuration.setText(StringUtil
                            .formartTemperature(locationInfo.forecastSummaryResult.forecastOfDay2.temperatureLowest) + " / " + StringUtil
                                            .formartTemperature(locationInfo.forecastSummaryResult.forecastOfDay2.temperatureHighest)
                                    + "℃");
                    
                }
                // 获取第三天的天气
                WeatherType weatherLastWeatherType = WeatherType
                        .getWeatherTypeById(locationInfo.forecastSummaryResult.forecastOfDay3.weatherType.ID);
                if (null != weatherLastWeatherType) {
                    lastDayWeatherStatus.setText(weatherLastWeatherType.description);
                    ivLastDay.setBackgroundResource(weatherLastWeatherType.getWeatherDescriptor()
                            .getIcon());
                    lastDayTempDuration.setText(StringUtil
                            .formartTemperature(locationInfo.forecastSummaryResult.forecastOfDay3.temperatureLowest) + " / " + StringUtil
                                            .formartTemperature(locationInfo.forecastSummaryResult.forecastOfDay3.temperatureHighest)
                                    + "℃");
                    
                }
            }
            btnMenu.setOnClickListener(onClickListener);
            btnRefreshWeather.setOnClickListener(onClickListener);
            container.addView(view);
            return view;
        }

        @ Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            RelativeLayout view = (RelativeLayout) object;
            view.clearAnimation();
            view.removeAllViews();
            container.removeView(view);
        }
    }

    /**
     * Adapter for hot city.
     * 
     * @author kurtis
     */
    private class CityAdapter extends BaseAdapter {

        @ Override
        public int getCount() {
            return (null != defaultHotCityList) ? defaultHotCityList.length : 0;
        }

        @ Override
        public Object getItem(int position) {
            return null;
        }

        @ Override
        public long getItemId(int position) {
            return 0;
        }

        @ Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tvCityName = new TextView(MainActivity.this);
            tvCityName.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, 100));
            tvCityName.setTextAppearance(MainActivity.this, R.style.gray_02_17);
            tvCityName.setPadding(10, 10, 10, 10);
            tvCityName.setText(defaultHotCityList[position]);
            tvCityName.setGravity(Gravity.CENTER);
            return tvCityName;
        }
    };

    /**
     * Click detail hot city.
     */
    private final OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @ Override
        public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
            // Insert City info
        	if (searchCityEditText != null && MainActivity.this.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) { 
            	//关闭输入法 
            	InputMethodManager imm = (InputMethodManager) getApplicationContext() .getSystemService(Context.INPUT_METHOD_SERVICE); 
            	imm.hideSoftInputFromWindow(searchCityEditText.getWindowToken(), 0);
            }
            if( 0 == position){
                LocationManager.requestWeatherLocation(new Callback <WeatherLocation, Error>() {

                    @ Override
                    public void success(final WeatherLocation location) {
                        new Thread(new Runnable() {

                            @ Override
                            public void run() {
                                LocationManager.saveLocation(location);
                            }

                        }).start();
                        if( null != location && location.city != null){
                            addCityInBackend(location.city.substring(0, location.city.length()-1));
                        }
                    }

                    @ Override
                    public void failure(Error error) {
                        Log.i(TAG, "error : " + error.getErrorMsg());
                    }

                });
            } else {
                addCityInBackend(defaultHotCityList[position]);
            }
        }

    };

    /**
     * Monitor the search content changed listener.
     */
    private final TextWatcher searchWatcher = new TextWatcher() {

        @ Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @ Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @ Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s.toString())) {
                return;
            }
            getSeachResult(s.toString());
            if (null != seachResultCityArrayList && seachResultCityArrayList.length > 0) {
                adapter = new ArrayAdapter <String>(MainActivity.this, // 定义匹配源的adapter
                        android.R.layout.simple_dropdown_item_1line, seachResultCityArrayList);
                searchCityEditText.setAdapter(adapter);
                searchCityEditText.showDropDown();
                adapter.notifyDataSetChanged();
            }
        }
    };

    private void getSeachResult(String cityName) {
        if (TextUtils.isEmpty(cityName)) {
            seachResultCityArrayList = new String [] {};
            return;
        }
        List <Map <String, String>> seachResultCityList = CityDao
                .getCityInfoByCityNameForuzzy(cityName);
        if (null != seachResultCityList && seachResultCityList.size() > 0) {
            seachResultCityArrayList = new String [seachResultCityList.size()];
            for (int i = 0; i < seachResultCityList.size(); i++) {
                seachResultCityArrayList[i] = seachResultCityList.get(i).get(
                        CityDao.ASSETS_CITY_TABLE_COLUMN_CITY_NAME_CN);
            }
        }
    }

    /**
     * Click detail hot city.
     */
    private final OnItemClickListener onSearchItemClickListener = new AdapterView.OnItemClickListener() {

        @ Override
        public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
            // Insert City info.
            LogUtil.i(TAG, "Insert City info");
            if (searchCityEditText != null && MainActivity.this.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) { 
            	//关闭输入法 
            	InputMethodManager imm = (InputMethodManager) getApplicationContext() .getSystemService(Context.INPUT_METHOD_SERVICE); 
            	imm.hideSoftInputFromWindow(searchCityEditText.getWindowToken(), 0);
            }
            TextView tvCityName = (TextView) view;
            addCityInBackend(tvCityName.getText().toString());
            imm.hideSoftInputFromWindow(searchCityEditText.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };

    private void addCityInBackend(String cityName) {
        MyCitiesForecastManager.requestMyCitiesForecastSummary(cityName,
                new Callback <MyCitiesForecastSummaryResult, Error>() {

                    @ Override
                    public void success(MyCitiesForecastSummaryResult result) {
                        LogUtil.d(TAG, "requestMyForecastSummary success");
                        LogUtil.d(TAG, StringUtil.toJson(result));

                        sortAndDeleteCity();

                        Toast.makeText(MainActivity.this, R.string.msg_add_city_success,
                                Toast.LENGTH_LONG).show();
                    }

                    @ Override
                    public void failure(Error error) {
                        LogUtil.d(TAG, "requestForecastSummary failure");
                        LogUtil.d(TAG, StringUtil.toJson(error));
                        Toast.makeText(MainActivity.this, R.string.msg_add_city_failure,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
    
    AnimationListener customAnimationListener = new AnimationListener(){

        @ Override
        public void onAnimationEnd(Animation arg0) {
            getLocationInfoList();
            pagerAdapter = new LocationInfoPagerAdapter(MainActivity.this);
            locationViewPager.setAdapter(pagerAdapter);
        }

        @ Override
        public void onAnimationRepeat(Animation arg0) {
            
        }

        @ Override
        public void onAnimationStart(Animation arg0) {
        }};
  
    public void startRotateAnimation(View view) {

        Animation animation = new RotateAnimation(0.0f, +360.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        view.setAnimation(animation);
        view.startAnimation(animation);
        animation.setAnimationListener(customAnimationListener);
    }
}
