package com.kindazrael.tingweather.ui.animator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.kindazrael.tingweather.R;
import com.kindazrael.tingweather.util.DhUtil;
import com.kindazrael.tingweather.util.MathUtil;

public class ShowerAnimator {

    private static final int MSG_RAIN = 1;
    private static final float YSTART = -70;
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final int RAIN_HEIGHT = 33;
    private static final int RAIN_WIDTH = 22;
    private static final int X_START = 18; // dip
    private static final int X_END = 142; // dip
    private static final int Y_END_FROM = 80; // dip
    private static final int Y_END_TO = 120; // dip

    private final Handler handler;
    private final RelativeLayout relativeLayout;
    private final Context context;
    private Timer timer;
    private TimerTask rainTask;
    private final int w;
    private final int h;
    private final List <ImageView> rainList;

    private final int period; // = 200; //ms
    private final int durationStart;// = 600; // ms
    private final int durationEnd;// = 1200; // ms

    private int count = 0;
    private final int countMax;

    static class ShowerRandomRainHandler extends Handler {

        private final WeakReference <ShowerAnimator> animatorReference;

        public ShowerRandomRainHandler (ShowerAnimator animator) {
            this.animatorReference = new WeakReference <ShowerAnimator>(animator);
        }

        @ Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_RAIN:
                    ShowerAnimator animator = animatorReference.get();
                    if (animator != null) {
                        animator.addRandomRain();
                    }
                    break;

                default:
                    break;
            }
        };
    }

    public ShowerAnimator (RelativeLayout relativeLayout, Context context, int period, int durationStart,
            int durationEnd, float scale, int countMax) {
        this.relativeLayout = relativeLayout;
        this.context = context;
        this.period = period;
        this.durationEnd = durationEnd;
        this.durationStart = durationStart;

        this.handler = new ShowerRandomRainHandler(this);

        this.w = (int) (DhUtil.dip2px(this.context, RAIN_WIDTH) * scale);
        this.h = (int) (DhUtil.dip2px(context, RAIN_HEIGHT) * scale);
        this.countMax = countMax;
        this.rainList = new ArrayList <ImageView>(this.countMax);
    }

    public void start() {
        this.count = 0;
        timer = new Timer();
        this.rainTask = new TimerTask() {

            @ Override
            public void run() {
                Message m = Message.obtain();
                m.what = MSG_RAIN;
                handler.sendMessage(m);
            }
        };
        timer.schedule(this.rainTask, 0, this.period);
    }

    private void addRandomRain() {
        if (this.count <= this.countMax) {
            final ImageView rain = getRain(this.count);
            count++;

            int x = DhUtil.dip2px(this.context, MathUtil.getRandom(X_START, X_END));
            int yEnd = DhUtil.dip2px(this.context, MathUtil.getRandom(Y_END_FROM, Y_END_TO));
            int duration = MathUtil.getRandom(durationStart, durationEnd);
            TranslateAnimation translateAnimation = new TranslateAnimation(x, x, YSTART, yEnd);
            translateAnimation.setInterpolator(ACCELERATE_INTERPOLATOR);
            translateAnimation.setDuration(duration);
            translateAnimation.setRepeatCount(Animation.INFINITE);
            translateAnimation.setRepeatMode(Animation.RESTART);

            rain.startAnimation(translateAnimation);

        } else {
            this.stopRain();
        }
    }
    
    private void stopRain() {
        this.rainTask.cancel();
        this.timer.cancel();

        handler.postDelayed(new Runnable() {
            
            @ Override
            public void run() {
                for (ImageView rain : rainList) {
                    rain.clearAnimation();
                    rain.setVisibility(View.INVISIBLE);
                }
            }
		}, 3000);
        
        handler.postDelayed(new Runnable() {
            
            @ Override
            public void run() {
                start();
            }
		}, 5000);
    }

    private ImageView getRain(int index) {
        if (this.rainList.size() <= this.countMax + 1) {
            final ImageView rain = new ImageView(this.context);
            rain.setBackgroundResource(R.drawable.rain);
            LayoutParams params = new LayoutParams(w, h);
            params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.rain_cloud_image_view);
            params.addRule(RelativeLayout.ALIGN_LEFT, R.id.rain_cloud_image_view);
            rain.setLayoutParams(params);
            rainList.add(rain);
            this.relativeLayout.addView(rain, 4);

            return rain;
        } else {
            ImageView rain = this.rainList.get(index);
            rain.setVisibility(View.VISIBLE);
            return rain;
        }
    }
}
