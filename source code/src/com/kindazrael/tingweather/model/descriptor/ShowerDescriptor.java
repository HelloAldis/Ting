package com.kindazrael.tingweather.model.descriptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kindazrael.tingweather.R;
import com.kindazrael.tingweather.ui.animator.AnimatorFactory;
import com.kindazrael.tingweather.ui.animator.ShowerAnimator;


public class ShowerDescriptor  extends WeatherTypeDescriptor {

    @ Override
    public int getIcon() {
        if (this.isNight()) {
            return R.drawable.icon_shower_night;
        } else {
            return R.drawable.icon_shower;
        }
    }

    @ Override
    public int getBackground() {
        if (this.isNight()) {
            return R.drawable.bg_main_night;
        } else {
            return R.drawable.bg_main_rain;
        }
    }
    
    @ Override
    public int getIconForCityList() {
        if (this.isNight()) {
            return R.drawable.icon_shower_night2;
        } else {
            return R.drawable.icon_shower2;
        }
    }

    @ Override
    public View getDisplayingView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.descriptor_rain, null);
        ShowerAnimator showerAnimator = AnimatorFactory.getShowerAnimator((RelativeLayout) view, context);
        showerAnimator.start();

        Animation cycleAnim = AnimationUtils.loadAnimation(context, R.anim.sunny_cloud_anim);

        ImageView imageView = (ImageView) view.findViewById(R.id.sun_bg_cloud_image_view);
        imageView.startAnimation(cycleAnim);
        imageView = (ImageView) view.findViewById(R.id.sun_cloud1_image_view);
        imageView.startAnimation(cycleAnim);

        cycleAnim = AnimationUtils.loadAnimation(context, R.anim.sunny_cloud_move_anim);
        imageView = (ImageView) view.findViewById(R.id.sun_cloud2_image_view);
        imageView.startAnimation(cycleAnim);
        imageView = (ImageView) view.findViewById(R.id.sun_cloud3_image_view);
        imageView.startAnimation(cycleAnim);

        cycleAnim = AnimationUtils.loadAnimation(context, R.anim.float_anim);
        imageView = (ImageView) view.findViewById(R.id.rain_cloud_image_view);
        imageView.startAnimation(cycleAnim);

        return view;
    }
}
