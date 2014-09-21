package com.kindazrael.tingweather.model.descriptor;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.kindazrael.tingweather.R;

public class CloudyDescriptor extends WeatherTypeDescriptor {

    @ Override
    public int getIcon() {
        if(this.isNight()) {
            return R.drawable.icon_cloudy_night;
        } else {
            return R.drawable.icon_cloudy;
        }
    }
    
    @ Override
    public int getIconForCityList() {
        if(this.isNight()) {
            return R.drawable.icon_cloudy_night2;
        } else {
            return R.drawable.icon_cloudy2;
        }
    }

    @ Override
    public int getBackground() {
        if (this.isNight()) {
            return R.drawable.bg_main_night;
        } else {
            return R.drawable.bg_main_cloudy;
        }
    }

    @ Override
    public View getDisplayingView(Context context) {
        final View view;
        if (this.isNight()) {
            view = LayoutInflater.from(context).inflate(R.layout.descriptor_cloudy_night, null);

            Animation moonAnim = AnimationUtils.loadAnimation(context, R.anim.sunny_moon_anim);
            ImageView imageView = (ImageView) view.findViewById(R.id.moon2_image_view);
            imageView.startAnimation(moonAnim);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.descriptor_cloudy, null);

            ImageView z1 = (ImageView) view.findViewById(R.id.cloudy_z_1_image_view);
            final Animation zAnim = AnimationUtils.loadAnimation(context, R.anim.cloudy_z_anim);
            final Animation z2Anim = AnimationUtils.loadAnimation(context, R.anim.cloudy_z_anim);
            final Animation z3Anim = AnimationUtils.loadAnimation(context, R.anim.cloudy_z_anim);
            z1.startAnimation(zAnim);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @ Override
                public void run() {
                    ImageView z2 = (ImageView) view.findViewById(R.id.cloudy_z_2_image_view);
                    z2.startAnimation(z2Anim);
                    z2.setVisibility(View.VISIBLE);

                }
            }, 1600);

            handler.postDelayed(new Runnable() {

                @ Override
                public void run() {
                    ImageView z3 = (ImageView) view.findViewById(R.id.cloudy_z_3_image_view);
                    z3.startAnimation(z3Anim);
                    z3.setVisibility(View.VISIBLE);
                }
            }, 3200);

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.float_anim);
            ImageView cloud = (ImageView) view.findViewById(R.id.cloudy_cloud_image_view);
            cloud.startAnimation(animation);
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
