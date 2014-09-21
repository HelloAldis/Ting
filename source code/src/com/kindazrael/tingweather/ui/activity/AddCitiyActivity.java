/*
 * Copyright 2014 Augmentum Inc. All rights reserved.
 */

package com.kindazrael.tingweather.ui.activity;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.kindazrael.tingweather.R;
import com.kindazrael.tingweather.base.BaseActivity;
import com.kindazrael.tingweather.common.Callback;
import com.kindazrael.tingweather.common.Error;
import com.kindazrael.tingweather.dao.CityDao;
import com.kindazrael.tingweather.manager.MyCitiesForecastManager;
import com.kindazrael.tingweather.model.MyCitiesForecastSummaryResult;
import com.kindazrael.tingweather.util.LogUtil;
import com.kindazrael.tingweather.util.StringUtil;

public class AddCitiyActivity extends BaseActivity {

    private static final String TAG = "AddCitiyActivity"; 
    private Button btnBack = null;
    private Button btnLoaction = null;
    private GridView hotCityListGridView = null;
    private String defaultHotCityList[] = null;
    private String seachResultCityArrayList[] = new String[]{};
    private List<Map<String, String>> seachResultCityList = null;
    private AutoCompleteTextView searchCityEditText = null;
    private ArrayAdapter<String> adapter = null;

    @ Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
    }

    /**
     * Initialize the views.
     */
    private void initializeUI() {
        setCustomContentView(R.layout.activity_add_city,
                R.string.screen_name_city_select);
        btnBack = (Button) findViewById(R.id.nav_back_button);
        btnLoaction = (Button) findViewById(R.id.nav_location_button);
        btnBack.setVisibility(View.VISIBLE);
        btnLoaction.setVisibility(View.VISIBLE);

        hotCityListGridView = (GridView) findViewById(R.id.hot_city_list_grid_view);
        searchCityEditText = (AutoCompleteTextView) findViewById(R.id.search_name_edit_text);

        defaultHotCityList = getResources().getStringArray(
                R.array.hot_city_list);
        hotCityListGridView.setAdapter(new CityAdapter());
        hotCityListGridView.setOnItemClickListener(onItemClickListener);
        searchCityEditText.addTextChangedListener(searchWatcher);
        
        adapter = new ArrayAdapter<String>(AddCitiyActivity.this, //定义匹配源的adapter
                android.R.layout.simple_dropdown_item_1line, seachResultCityArrayList);
        searchCityEditText.setAdapter(adapter);
        searchCityEditText.setOnItemSelectedListener(onItemSelectedListener);
        
        btnBack.setOnClickListener(onClickListener);
    }
        
    /**
     * OnClickListener to handler all click event.
     */
    private final OnClickListener onClickListener = new OnClickListener() {
        
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
    
    
    /**
     * Monitor the search content changed listener.
     */
    private final TextWatcher searchWatcher = new TextWatcher() {
        
        @ Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            
        }
        
        @ Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
            
        }
        
        @ Override
        public void afterTextChanged(Editable s) {
            getSeachResult(s.toString());
            adapter = new ArrayAdapter<String>(AddCitiyActivity.this, //定义匹配源的adapter
                    android.R.layout.simple_dropdown_item_1line, seachResultCityArrayList);
            searchCityEditText.setAdapter(adapter);
            //searchCityEditText.setThreshold(1);
            searchCityEditText.showDropDown();
        }
    };
    
    /**
     * Adapter for hot city.
     * @author kurtis
     *
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
            TextView tvCityName = new TextView(AddCitiyActivity.this);
            tvCityName.setLayoutParams(new GridView.LayoutParams(
                    LayoutParams.MATCH_PARENT, 100));
            tvCityName.setPadding(10, 10, 10, 10);
            tvCityName.setText(defaultHotCityList[position]);
            tvCityName.setGravity(Gravity.CENTER);
            tvCityName.setBackgroundResource(R.drawable.bg_add_more_city);
            return tvCityName;
        }
    };

    /**
     * Click detail hot city.
     */
    private final OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @ Override
        public void onItemClick(AdapterView <?> parent, View view,
                int position, long id) {
        	InputMethodManager imm = (InputMethodManager) AddCitiyActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        	// 得到InputMethodManager的实例
        	if (imm.isActive()) {
        		imm.hideSoftInputFromWindow(AddCitiyActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        	}
         // Insert City info
            
            MyCitiesForecastManager.requestMyCitiesForecastSummary(defaultHotCityList[position], 
                    new Callback <MyCitiesForecastSummaryResult, Error>() {

                        @ Override
                        public void success(MyCitiesForecastSummaryResult result) {
                            LogUtil.d(TAG, "requestMyForecastSummary success");
                            LogUtil.d(TAG, StringUtil.toJson(result));

                            finish();
                        }

                        @ Override
                        public void failure(Error error) {
                            LogUtil.d(TAG, "requestForecastSummary failure");
                            LogUtil.d(TAG, StringUtil.toJson(error));
                        }
                    });
        }

    };
    
    /**
     * Selected the search result list.
     */
    private final OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

        @ Override
        public void onItemSelected(AdapterView <?> parent, View view,
                int position, long id) {
         // Insert City info.
            LogUtil.i(TAG, "Insert City info");
            InputMethodManager imm = (InputMethodManager) AddCitiyActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        	// 得到InputMethodManager的实例
        	if (imm.isActive()) {
        		imm.hideSoftInputFromWindow(AddCitiyActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        	}
        }

        @ Override
        public void onNothingSelected(AdapterView <?> parent) {
        	InputMethodManager imm = (InputMethodManager) AddCitiyActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        	// 得到InputMethodManager的实例
        	if (imm.isActive()) {
        		imm.hideSoftInputFromWindow(AddCitiyActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        	}
        }
        
    };
    
    private void getSeachResult(String cityName) {
        if(TextUtils.isEmpty(cityName)) {
            seachResultCityArrayList = new String[]{};
            return;
        }
        seachResultCityList = CityDao.getCityInfoByCityNameForuzzy(cityName);
        if(null != seachResultCityList && seachResultCityList.size() > 0) {
            seachResultCityArrayList = new String[seachResultCityList.size()];
            for (int i = 0; i< seachResultCityList.size() ; i++) {
                seachResultCityArrayList[i] = seachResultCityList.get(i).get(CityDao.ASSETS_CITY_TABLE_COLUMN_CITY_NAME_CN);
            } 
        } 
    }
}
