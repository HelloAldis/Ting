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

public class SnowAnimator {

    private static final int MSG_SNOW = 2;
    private static final float YSTART = -70;
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final int X_START = 10; // dip
    private static final int X_END = 135; // dip
    private static final int Y_END_FROM = 80; // dip
    private static final int Y_END_TO = 120; // dip
    private static final int SNOW_WIDTH = 30;
    private static final int SNOW_HEIGHT = 30;

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

    static class RandomSnowHandler extends Handler {

        private final WeakReference <SnowAnimator> animatorReference;

        public RandomSnowHandler (SnowAnimator animator) {
            this.animatorReference = new WeakReference <SnowAnimator>(animator);
        }

        @ Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_SNOW:
                    SnowAnimator animator = animatorReference.get();
                    if (animator != null) {
                        animator.addRandomSnow();
                    }
                    break;

                default:
                    break;
            }
        };
    }

    public SnowAnimator (RelativeLayout relativeLayout, Context context, int period, int durationStart,
            int durationEnd, float scale, long countMax) {
        this.task = new TimerTask() {

            @ Override
            public void run() {
                Message message = Message.obtain();
                message.what = MSG_SNOW;
                handler.sendMessage(message);
            }
        };

        this.relativeLayout = relativeLayout;
        this.context = context;
        this.period = period;
        this.durationEnd = durationEnd;
        this.durationStart = durationStart;

        this.handler = new RandomSnowHandler(this);
        this.w = (int) (DhUtil.dip2px(this.context, SNOW_WIDTH) * scale);
        this.h = (int) (DhUtil.dip2px(context, SNOW_HEIGHT) * scale);
        
        this.countMax = countMax;
    }

    public void start() {
        timer = new Timer();
        timer.schedule(this.task, 0, this.period);
    }

    private void addRandomSnow() {
        if (this.count <= this.countMax) {
            final ImageView snow = new ImageView(this.context);
            snow.setBackgroundResource(R.drawable.snow);
            LayoutParams params = new LayoutParams(this.w, this.h);
            params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.snow_cloud_image_view);
            params.addRule(RelativeLayout.ALIGN_LEFT, R.id.snow_cloud_image_view);
            snow.setLayoutParams(params);
            count++;
            this.relativeLayout.addView(snow, 5);

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
            // public void onAnimationStart(Animation arg0) {
            // // Do nothing
            // }
            //
            // @ Override
            // public void onAnimationRepeat(Animation arg0) {
            // // Do nothing
            // }
            //
            // @ Override
            // public void onAnimationEnd(Animation arg0) {
            // snow.clearAnimation();
            // relativeLayout.removeView(snow);
            // queue.add(snow);
            // }
            // });

            snow.startAnimation(translateAnimation);
        } else {
            this.task.cancel();
        }
    }
}
