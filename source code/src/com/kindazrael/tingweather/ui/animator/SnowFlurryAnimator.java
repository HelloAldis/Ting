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

public class SnowFlurryAnimator {

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
    private final RelativeLayout relativeLayout;
    private final Context context;
    private Timer timer;
    private TimerTask snowTask;
    private final int w;
    private final int h;
    private final List <ImageView> snowList;

    private final int period; // = 200; //ms
    private final int durationStart;// = 600; // ms
    private final int durationEnd;// = 1200; // ms

    private int count = 0;
    private final int countMax;

    static class FlurryRandomSnowHandler extends Handler {

        private final WeakReference <SnowFlurryAnimator> animatorReference;

        public FlurryRandomSnowHandler(SnowFlurryAnimator animator) {
            this.animatorReference = new WeakReference <SnowFlurryAnimator>(animator);
        }

        @ Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_SNOW:
                    SnowFlurryAnimator animator = animatorReference.get();
                    if (animator != null) {
                        animator.addRandomSnow();
                    }
                    break;

                default:
                    break;
            }
        };
    }

    public SnowFlurryAnimator (RelativeLayout relativeLayout, Context context, int period, int durationStart,
            int durationEnd, float scale, int countMax) {
        this.relativeLayout = relativeLayout;
        this.context = context;
        this.period = period;
        this.durationEnd = durationEnd;
        this.durationStart = durationStart;

        this.handler = new FlurryRandomSnowHandler(this);
        this.w = (int) (DhUtil.dip2px(this.context, SNOW_WIDTH) * scale);
        this.h = (int) (DhUtil.dip2px(context, SNOW_HEIGHT) * scale);

        this.countMax = countMax;
        this.snowList = new ArrayList <ImageView>(this.countMax);
    }

    public void start() {
        this.count = 0;
        timer = new Timer();
        this.snowTask = new TimerTask() {

            @ Override
            public void run() {
                Message m = Message.obtain();
                m.what = MSG_SNOW;
                handler.sendMessage(m);
            }
        };
        timer.schedule(this.snowTask, 0, this.period);
    }

    private void addRandomSnow() {
        if (this.count <= this.countMax) {
            final ImageView snow = getRain(this.count);
            count++;

            int x = DhUtil.dip2px(this.context, MathUtil.getRandom(X_START, X_END));
            int yEnd = DhUtil.dip2px(this.context, MathUtil.getRandom(Y_END_FROM, Y_END_TO));
            int duration = MathUtil.getRandom(durationStart, durationEnd);
            TranslateAnimation translateAnimation = new TranslateAnimation(x, x, YSTART, yEnd);
            translateAnimation.setInterpolator(ACCELERATE_INTERPOLATOR);
            translateAnimation.setDuration(duration);
            translateAnimation.setRepeatCount(Animation.INFINITE);
            translateAnimation.setRepeatMode(Animation.RESTART);

            snow.startAnimation(translateAnimation);

        } else {
            this.stopRain();
        }
    }

    private void stopRain() {
        this.snowTask.cancel();
        this.timer.cancel();

        handler.postDelayed(new Runnable() {

            @ Override
            public void run() {
                for (ImageView snow : snowList) {
                    snow.clearAnimation();
                    snow.setVisibility(View.INVISIBLE);
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
        if (this.snowList.size() <= this.countMax + 1) {
            final ImageView snow = new ImageView(this.context);
            snow.setBackgroundResource(R.drawable.snow);
            LayoutParams params = new LayoutParams(this.w, this.h);
            params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.snow_cloud_image_view);
            params.addRule(RelativeLayout.ALIGN_LEFT, R.id.snow_cloud_image_view);
            snow.setLayoutParams(params);
            snowList.add(snow);
            this.relativeLayout.addView(snow, 4);

            return snow;
        } else {
            ImageView snow = this.snowList.get(index);
            snow.setVisibility(View.VISIBLE);
            return snow;
        }
    }
}
