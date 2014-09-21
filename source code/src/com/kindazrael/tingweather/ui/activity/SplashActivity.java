/*
 * Copyright 2014 Augmentum Inc. All rights reserved.
 */

package com.kindazrael.tingweather.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.kindazrael.tingweather.R;
import com.kindazrael.tingweather.base.BaseActivity;
import com.kindazrael.tingweather.common.Callback;
import com.kindazrael.tingweather.common.Error;
import com.kindazrael.tingweather.location.LocationManager;
import com.kindazrael.tingweather.model.WeatherLocation;
import com.kindazrael.tingweather.util.SharedPreferencesUtil;

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getName();

    private ImageView splashImage = null;

    @ Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashImage = (ImageView) findViewById(R.id.splash_image_view);

        LocationManager.requestWeatherLocation(new Callback <WeatherLocation, Error>() {

            @ Override
            public void success(final WeatherLocation location) {
                new Thread(new Runnable() {

                    @ Override
                    public void run() {
                        LocationManager.saveLocation(location);
                    }

                }).start();
            }

            @ Override
            public void failure(Error error) {
                Log.i(TAG, "error : " + error.getErrorMsg());
            }

        });

        splashImage.postDelayed(new Runnable() {

            @ Override
            public void run() {
                boolean isNotFristInstall = SharedPreferencesUtil
                        .getBooleanValue(SharedPreferencesUtil.PREFERENCE_KEY_ISFRISTINSTALL);
                if (isNotFristInstall) {
                    // Display welcome page if it's the first install page.
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 3000);
    }

}
