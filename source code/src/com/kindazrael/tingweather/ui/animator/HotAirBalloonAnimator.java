package com.kindazrael.tingweather.ui.animator;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.view.View;
import android.view.animation.Animation;

public class HotAirBalloonAnimator {

    private final ObjectAnimator xObjectAnimator;
    private final ObjectAnimator yObjectAnimator;
    private final View targetView;

    private static final long DURATION = 40 * 1000;

    private class XEvaluator implements TypeEvaluator <Float> {

        @ Override
        public Float evaluate(float fraction, Float startValue, Float endValue) {
            return startValue + fraction * (endValue - startValue);
        }

    }

    private class YEvaluator implements TypeEvaluator <Float> {

        @ Override
        public Float evaluate(float fraction, Float startValue, Float endValue) {
            return (float) ((endValue - startValue) * Math.sin(Math.toRadians(HotAirBalloonAnimator.this.targetView
                    .getTranslationX())));
        }

    }

    public HotAirBalloonAnimator (View view) {
        this.targetView = view;
        this.xObjectAnimator = ObjectAnimator.ofObject(view, "translationX", new XEvaluator(), Float.valueOf(0),
                Float.valueOf(1000));
        this.xObjectAnimator.setDuration(DURATION);
        this.xObjectAnimator.setRepeatCount(Animation.INFINITE);
        this.xObjectAnimator.setRepeatMode(Animation.RESTART);
        
        this.yObjectAnimator = ObjectAnimator.ofObject(view, "translationY", new YEvaluator(), Float.valueOf(0),
                Float.valueOf(100));
        this.yObjectAnimator.setDuration(DURATION);
        this.yObjectAnimator.setRepeatCount(Animation.INFINITE);
        this.yObjectAnimator.setRepeatMode(Animation.RESTART);
        
    }

    public void start() {
        this.xObjectAnimator.start();
        this.yObjectAnimator.start();
    }

    public void stop() {
        this.xObjectAnimator.cancel();
        this.yObjectAnimator.cancel();
        this.targetView.clearAnimation();
    }
}
