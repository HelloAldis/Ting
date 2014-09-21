package com.kindazrael.tingweather.model.descriptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.kindazrael.tingweather.R;
import com.kindazrael.tingweather.ui.animator.AnimatorFactory;
import com.kindazrael.tingweather.ui.animator.HotAirBalloonAnimator;


public class HazeDescriptor extends WeatherTypeDescriptor {

    @ Override
    public int getIcon() {
        return R.drawable.icon_haze;
    }

    @ Override
    public int getBackground() {
        if (this.isNight()) {
            return R.drawable.bg_main_night;
        } else {
            return R.drawable.bg_main_sunny;
        }
    }
    
    @ Override
    public int getIconForCityList() {
        return R.drawable.icon_haze;
    }

    @ Override
    public View getDisplayingView(Context context) {
        View view = null;

        if (this.isNight()) {
            view = LayoutInflater.from(context).inflate(R.layout.descriptor_sunny_night, null);

            Animation moonAnim = AnimationUtils.loadAnimation(context, R.anim.sunny_moon_anim);
            ImageView imageView = (ImageView) view.findViewById(R.id.moon2_image_view);
            imageView.startAnimation(moonAnim);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.descriptor_haze, null);

            ImageView hotAirBalloon = (ImageView) view.findViewById(R.id.sun_hot_air_balloon_image_view);
            HotAirBalloonAnimator animator = AnimatorFactory.getHotAirBalloonAnimator(hotAirBalloon);
            animator.start();
            hotAirBalloon.setTag(animator);

            Animation sunAnim = AnimationUtils.loadAnimation(context, R.anim.float_anim);
            ImageView imageView = (ImageView) view.findViewById(R.id.sun_sad_image_view);
            imageView.startAnimation(sunAnim);
        }

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

        return view;
    }

}
