package com.kindazrael.tingweather.model.descriptor;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.kindazrael.tingweather.R;


public class OvercastDescriptor extends WeatherTypeDescriptor {

    @ Override
    public int getIcon() {
        return R.drawable.icon_overcast;
    }

    @ Override
    public int getBackground() {
        if (this.isNight()) {
            return R.drawable.bg_main_night;
        } else {
            return R.drawable.bg_main_overcast;
        }
    }
    
    @ Override
    public int getIconForCityList() {
    	return R.drawable.icon_overcast2;
    }

    @ Override
    public View getDisplayingView(Context context) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.descriptor_overcast, null);

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
        
        imageView = (ImageView) view.findViewById(R.id.overcast_crow_image_view);
        imageView.setPivotX(0.4f * imageView.getLayoutParams().width);
        imageView.setPivotY(0.66f * imageView.getLayoutParams().height);
        Keyframe kf0 = Keyframe.ofFloat(0f, 0);
        Keyframe kf1 = Keyframe.ofFloat(0.1f, -25);
        Keyframe kf2 = Keyframe.ofFloat(0.2f, 33);
        Keyframe kf3 = Keyframe.ofFloat(0.23f, 20);
        Keyframe kf4 = Keyframe.ofFloat(0.26f, 33);
        Keyframe kf5 = Keyframe.ofFloat(0.35f, 33);
        Keyframe kf6 = Keyframe.ofFloat(0.5f, 0);
        Keyframe kf7 = Keyframe.ofFloat(1f, 0);
        PropertyValuesHolder propertyValuesHolderRotationHolder = PropertyValuesHolder.ofKeyframe("rotation", kf0, kf1,
                kf2, kf3, kf4, kf5, kf6, kf7);
        ObjectAnimator rotaAnimator = ObjectAnimator.ofPropertyValuesHolder(imageView,
                propertyValuesHolderRotationHolder);
        rotaAnimator.setDuration(3000);
        rotaAnimator.setRepeatMode(Animation.RESTART);
        rotaAnimator.setRepeatCount(Animation.INFINITE);
        rotaAnimator.start();

        return view;
    }

}
