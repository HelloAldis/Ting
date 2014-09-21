package com.kindazrael.tingweather.model.descriptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.kindazrael.tingweather.R;
import com.kindazrael.tingweather.ui.animator.AnimatorFactory;
import com.kindazrael.tingweather.ui.animator.HotAirBalloonAnimator;


public class SunnyDescriptor extends WeatherTypeDescriptor {

    @ Override
    public int getIcon() {
        if (this.isNight()) {
            return R.drawable.icon_sunny_night;
        } else {
            return R.drawable.icon_sunny;
        }
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
    	if (this.isNight()) {
            return R.drawable.icon_sunny_night2;
        } else {
            return R.drawable.icon_sunny2;
        }
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
            view = LayoutInflater.from(context).inflate(R.layout.descriptor_sunny, null);

            Animation sunAnim = AnimationUtils.loadAnimation(context, R.anim.float_anim);
            ImageView imageView = (ImageView) view.findViewById(R.id.sun_image_view);
            imageView.startAnimation(sunAnim);

            ImageView hotAirBalloon = (ImageView) view.findViewById(R.id.sun_hot_air_balloon_image_view);
            HotAirBalloonAnimator animator = AnimatorFactory.getHotAirBalloonAnimator(hotAirBalloon);
            animator.start();
            hotAirBalloon.setTag(animator);
            addOnTouchListenerForHotAirBalloon(hotAirBalloon);
        }

        Animation cloundAnim = AnimationUtils.loadAnimation(context, R.anim.sunny_cloud_anim);

        ImageView imageView = (ImageView) view.findViewById(R.id.sun_bg_cloud_image_view);
        imageView.startAnimation(cloundAnim);
        imageView = (ImageView) view.findViewById(R.id.sun_cloud1_image_view);
        imageView.startAnimation(cloundAnim);

        cloundAnim = AnimationUtils.loadAnimation(context, R.anim.sunny_cloud_move_anim);
        imageView = (ImageView) view.findViewById(R.id.sun_cloud2_image_view);
        imageView.startAnimation(cloundAnim);
        imageView = (ImageView) view.findViewById(R.id.sun_cloud3_image_view);
        imageView.startAnimation(cloundAnim);

        return view;
    }

    private void addOnTouchListenerForHotAirBalloon(ImageView view) {
        view.setOnTouchListener(new OnTouchListener() {
            
            private float previousX;
            private float previousY;
            private float currentX;
            private float currentY;

            @ Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        this.previousX = event.getRawX();
                        this.previousY = event.getRawY();
                        this.currentX = this.previousX;
                        this.currentY = this.previousY;
                        HotAirBalloonAnimator animator = (HotAirBalloonAnimator) view.getTag();
                        animator.stop();

                        break;
                    case MotionEvent.ACTION_UP:
                        // hotAirBalloonAnimator.start();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        this.currentX = event.getRawX();
                        this.currentY = event.getRawY();

                        float diffX = this.currentX - this.previousX;
                        float diffY = this.currentY - this.previousY;

                        view.setTranslationX(view.getTranslationX() + diffX);
                        view.setTranslationY(view.getTranslationY() + diffY);

                        this.previousX = this.currentX;
                        this.previousY = this.currentY;
                        break;

                    default:
                        break;
                }

                return false;
            }
        });
    }
}
