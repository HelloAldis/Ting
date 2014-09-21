/*
 * Copyright 2014 Augmentum Inc. All rights reserved.
 */

package com.kindazrael.tingweather.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.kindazrael.tingweather.R;

public abstract class BaseActivity extends Activity {

    private TextView tvTitle = null;

    @ Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Set content view and title using the custom navigation title bar.
     * 
     * @param activityLayoutRes
     *            -- Current screen layout resource index.
     * @param titleRes
     *            -- Current screen title resource index.
     */
    public void setCustomContentView(int activityLayoutRes, int titleRes) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(activityLayoutRes);
        getWindow()
                .setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.nav_bar);
        tvTitle = (TextView) findViewById(R.id.nav_screen_title);
        setTitle(titleRes);
    }

    /**
     * Set screen title.
     * 
     * @param titleRes
     *            -- Current screen title resource index.
     */
    @Override
	public void setTitle(int titleRes) {
        if (null != tvTitle) {
            tvTitle.setText(titleRes);
        }
    }
}
