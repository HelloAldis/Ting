
package com.kindazrael.tingweather.ui.widgets;

import java.util.List;

import com.kindazrael.tingweather.R;
import com.kindazrael.tingweather.ui.activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 
 * @author victor--2013-5-23----com.selfimpr.adapter
 *
 */
public class ViewPagerAdapter extends PagerAdapter {

	private List<View> data;
	private Activity activity;

	public ViewPagerAdapter(List<View> data, Activity activity) {
		this.data = data;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager) container).addView(data.get(position));
		if(data.size()-1==position){//判断导航页是不是最后一页
			Button submit = (Button) container.findViewById(R.id.guide_start_app);
			submit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					redirect();
				}
			});
			
		}
		return data.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	private void redirect(){
		Intent intent = new Intent(activity,MainActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}
}
