/*
 * Copyright 2014 Augmentum Inc. All rights reserved.
 */

package com.kindazrael.tingweather.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

import com.kindazrael.tingweather.R;
import com.kindazrael.tingweather.base.BaseActivity;
import com.kindazrael.tingweather.ui.widgets.ViewPagerAdapter;
import com.kindazrael.tingweather.util.SharedPreferencesUtil;

public class WelcomeActivity extends BaseActivity implements OnPageChangeListener {

	private ViewPagerAdapter adapter;
	private ViewPager pager;
	private List<View> views;//视图数据
	private int[] imageVeiwResourceId = { R.drawable.iweather1,
			R.drawable.iweather2, R.drawable.iweather3 };//显示图片的数据
	private ImageView[] point;//底部小圆点
	private int currentId = 0;//当前ID

    @ Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 去掉信息栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        setContentView(R.layout.activity_welcome);
        SharedPreferencesUtil.setBooleanValue(
                SharedPreferencesUtil.PREFERENCE_KEY_ISFRISTINSTALL, false);
        initView();
        setPoint();//第一次设置小圆点位置
    }
    
    /**初始化view
	 * 
	 */
	private void initView() {
		pager = (ViewPager) this.findViewById(R.id.vp);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		views = new ArrayList<View>();
		
		for (int i = 0; i < imageVeiwResourceId.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(imageVeiwResourceId[i]);
			imageView.setScaleType(ScaleType.FIT_XY);
			views.add(imageView);
		}
		
		View view = inflater.inflate(R.layout.last_guide, null);
		views.add(view);
		adapter = new ViewPagerAdapter(views, WelcomeActivity.this);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(this);
	}
    
    /**设置小圆点
	 * 
	 */
	private void setPoint() {
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.viewpager_ll);
		point = new ImageView[ll.getChildCount()];
		for (int i = 0; i < ll.getChildCount(); i++) {
			if (currentId == i) {
				point[i] = (ImageView) ll.getChildAt(i);
				point[i].setImageResource(R.drawable.point_focus);
			} else {
				point[i] = (ImageView) ll.getChildAt(i);
				point[i].setImageResource(R.drawable.point_normal);
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO 滑动状态监听方法

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO 滑动是的监听方法
	}

	@Override
	public void onPageSelected(int arg0) {
		//TODO 选择页面的监听方法
		currentId = arg0;
		setPoint();

	}
}
