package com.kindazrael.tingweather.ui.animator;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.kindazrael.tingweather.R;
import com.kindazrael.tingweather.util.DhUtil;
import com.kindazrael.tingweather.util.MathUtil;


public class RainAnimator {
    
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
    private final TimerTask task;
    private final RelativeLayout relativeLayout;
    private final Context context;
    private Timer timer;
    private final int w;
    private final int h;
    
    private final int period; // = 200; //ms
    private final int durationStart;// = 600; // ms
    private final int durationEnd;// = 1200; // ms

    private long count = 0;
    private final long countMax;

    static class RandomRainHandler extends Handler {

        private final WeakReference <RainAnimator> animatorReference;

        public RandomRainHandler (RainAnimator animator) {
            this.animatorReference = new WeakReference <RainAnimator>(animator);
        }

        @ Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_RAIN:
                    RainAnimator animator = animatorReference.get();
                    if (animator != null) {
                        animator.addRandomRain();
                    }
                    break;

                default:
                    break;
            }
        };
    }

    public RainAnimator (RelativeLayout relativeLayout, Context context, int period, int durationStart,
            int durationEnd, float scale, long countMax) {
        this.task = new TimerTask() {

            @ Override
            public void run() {
                Message m = Message.obtain();
                m.what = MSG_RAIN;
                handler.sendMessage(m);
            }
        };

        this.relativeLayout = relativeLayout;
        this.context = context;
        this.period = period;
        this.durationEnd = durationEnd;
        this.durationStart = durationStart;

        this.handler = new RandomRainHandler(this);

        this.w = (int) (DhUtil.dip2px(this.context, RAIN_WIDTH) * scale);
        this.h = (int) (DhUtil.dip2px(context, RAIN_HEIGHT) * scale);
        this.countMax = countMax;
    }

    public void start() {
        timer = new Timer();
        timer.schedule(this.task, 0, this.period);
    }

    private void addRandomRain() {
        if (this.count <= this.countMax) {
            final ImageView rain = new ImageView(this.context);
            rain.setBackgroundResource(R.drawable.rain);
            LayoutParams params = new LayoutParams(w, h);
            params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.rain_cloud_image_view);
            params.addRule(RelativeLayout.ALIGN_LEFT, R.id.rain_cloud_image_view);
            rain.setLayoutParams(params);
            count++;

            this.relativeLayout.addView(rain, 5);

            int x = DhUtil.dip2px(this.context, MathUtil.getRandom(X_START, X_END));
            int yEnd = DhUtil.dip2px(this.context, MathUtil.getRandom(Y_END_FROM, Y_END_TO));
            int duration = MathUtil.getRandom(durationStart, durationEnd);
            TranslateAnimation translateAnimation = new TranslateAnimation(x, x, YSTART, yEnd);
            translateAnimation.setInterpolator(ACCELERATE_INTERPOLATOR);
            translateAnimation.setDuration(duration);
            translateAnimation.setRepeatCount(Animation.INFINITE);
            translateAnimation.setRepeatMode(Animation.RESTART);
            // translateAnimation.setAnimationListener(new AnimationListener() {
            //
            // @ Override
            // public void onAnimationStart(Animation animation) {
            // // Do nothing
            // }
            //
            // @ Override
            // public void onAnimationRepeat(Animation animation) {
            // }
            //
            // @ Override
            // public void onAnimationEnd(Animation animation) {
            // // rain.clearAnimation();
            // // relativeLayout.removeView(rain);
            // }
            // });

            rain.startAnimation(translateAnimation);
        } else {
            this.task.cancel();
        }
    }
}
