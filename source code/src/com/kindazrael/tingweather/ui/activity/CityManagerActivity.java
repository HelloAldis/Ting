/*
 * Copyright 2014 Augmentum Inc. All rights reserved.
 */

package com.kindazrael.tingweather.ui.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kindazrael.tingweather.R;
import com.kindazrael.tingweather.base.BaseActivity;
import com.kindazrael.tingweather.manager.MyCitiesForecastManager;
import com.kindazrael.tingweather.model.MyCitiesForecastSummaryResult;
import com.kindazrael.tingweather.ui.widgets.ViewHolder;

public class CityManagerActivity extends BaseActivity {

    private GridView cityListGridView = null;
    private Button btnBack = null;
    private Button btnRefresh = null;
    private  List<MyCitiesForecastSummaryResult> selectedCity = null;

    @ Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
    }

    /**
     * Initialize the views.
     */
    private void initializeUI() {
        setCustomContentView(R.layout.activity_city_manager,
                R.string.screen_name_city_manager);
        btnBack = (Button) findViewById(R.id.nav_back_button);
        btnRefresh = (Button) findViewById(R.id.nav_refresh_button);
        btnBack.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
        cityListGridView = (GridView) findViewById(R.id.city_list_grid_view);
        
        btnBack.setOnClickListener(onClickListener);
    }

    @ Override
    protected void onResume() {
        super.onResume();
        getSelectedCity();
        cityListGridView.setAdapter(new CityListAdapter());
        cityListGridView.setOnItemClickListener(onItemClickListener);
    }

    private void getSelectedCity() {
        selectedCity = MyCitiesForecastManager.getAllMyCitiesForecastSummary();
    }
    /**
     * OnClickListener to handler all click event.
     */
    private OnClickListener onClickListener = new OnClickListener() {
        
        @ Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nav_back_button:
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

    private class CityListAdapter extends BaseAdapter {

        @ Override
        public int getCount() {
            return selectedCity.size() + 1;
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
            if (null == convertView) {
                convertView = LayoutInflater.from(CityManagerActivity.this)
                        .inflate(R.layout.item_city_manager_city_list, null);
            }

            ImageView ivAddCity = ViewHolder.get(convertView,
                    R.id.add_new_city_image_view);
            RelativeLayout cityInfoLayout = ViewHolder.get(convertView,
                    R.id.add_city_main_layout);
            TextView tvLowerTemperature = ViewHolder.get(convertView,
                    R.id.temperature_lower_text_view);
            TextView tvHihgerTemperature = ViewHolder.get(convertView,
                    R.id.temperature_higher_text_view);
            TextView tvWeatherStatus = ViewHolder.get(convertView,
                    R.id.weather_status_text_view);
            TextView tvCityName = ViewHolder.get(convertView,
                    R.id.city_name_text_view);
            ImageView ivLoaction = ViewHolder.get(convertView,
                    R.id.icon_location);

            if (position == selectedCity.size()) {
                ivAddCity.setVisibility(View.VISIBLE);
                cityInfoLayout.setVisibility(View.GONE);
            } else {
                ivAddCity.setVisibility(View.GONE);
                cityInfoLayout.setVisibility(View.VISIBLE);

                MyCitiesForecastSummaryResult itemData = selectedCity.get(position);
                if(null != itemData.forecastSummaryResult && null != itemData.forecastSummaryResult.forecastOfDay1) {
                    tvLowerTemperature.setText(itemData.forecastSummaryResult.forecastOfDay1.temperatureLowest + "");
                    tvHihgerTemperature.setText(itemData.forecastSummaryResult.forecastOfDay1.temperatureHighest + "");
                    tvWeatherStatus.setText(itemData.forecastSummaryResult.forecastOfDay1.weatherType.description);
                }
               if (null != itemData.weatherLocation) {
                   tvCityName.setText(itemData.weatherLocation.city);
                   ivLoaction.setVisibility(View.VISIBLE);
               }
            }

            return convertView;
        }
    }

    private OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @ Override
        public void onItemClick(AdapterView <?> parent, View view,
                int position, long id) {
            if (position == selectedCity.size()) {
                Intent addCityIntent = new Intent(CityManagerActivity.this,
                        AddCitiyActivity.class);
                startActivity(addCityIntent);
            }
        }

    };
}
