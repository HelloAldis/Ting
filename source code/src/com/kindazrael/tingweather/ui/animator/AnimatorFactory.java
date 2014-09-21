package com.kindazrael.tingweather.ui.animator;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AnimatorFactory {

    public static final int SHOWER_PERIOD = 150;
    public static final int SHOWER_DURATION_START = 600;
    public static final int SHOWER_DURATION_END = 1200;
    public static final float SHOWER_SCALE = 0.4f;
    public static final int SHOWER_COUNT_MAX = 13;

    public static final int LIGHT_RAIN_PERIOD = 150;
    public static final int LIGHT_RAIN_DURATION_START = 600;
    public static final int LIGHT_RAIN_DURATION_END = 1200;
    public static final float LIGHT_RAIN_SCALE = 0.3f;
    public static final int LIGHT_RAIN_COUNT_MAX = 13;

    public static final int MODERATE_RAIN_PERIOD = 100;
    public static final int MODERATE_RAIN_DURATION_START = 600;
    public static final int MODERATE_RAIN_DURATION_END = 800;
    public static final float MODERATE_RAIN_SCALE = 0.45f;
    public static final int MODERATE_RAIN_COUNT_MAX = 26;

    public static final int HEAVY_RAIN_PERIOD = 80;
    public static final int HEAVY_RAIN_DURATION_START = 500;
    public static final int HEAVY_RAIN_DURATION_END = 800;
    public static final float HEAVY_RAIN_SCALE = 0.55f;
    public static final int HEAVY_RAIN_COUNT_MAX = 39;

    public static final int SLEET_PERIOD = 300;
    public static final int SLEET_DUATION_START = 1800;
    public static final int SLEET_DUATION_END = 2000;
    public static final float SLEET_SCALE = 0.3f;
    public static final int SLEET_COUNT_MAX = 13;

    public static final int FLURRY_PERIOD = 300;
    public static final int FLURRY_DUATION_START = 1800;
    public static final int FLURRY_DUATION_END = 2000;
    public static final float FLURRY_SCALE = 0.35f;
    public static final int FLURRY_COUNT_MAX = 9;

    public static final int LIGHT_SNOW_PERIOD = 300;
    public static final int LIGHT_SNOW_DUATION_START = 1800;
    public static final int LIGHT_SNOW_DUATION_END = 2000;
    public static final float LIGHT_SNOW_SCALE = 0.3f;
    public static final int LIGHT_SNOW_COUNT_MAX = 9;

    public static final int MODERATE_SNOW_PERIOD = 250;
    public static final int MODERATE_SNOW_DUATION_START = 1800;
    public static final int MODERATE_SNOW_DUATION_END = 2000;
    public static final float MODERATE_SNOW_SCALE = 0.45f;
    public static final int MODERATE_SNOW_COUNT_MAX = 10;

    public static final int HEAVY_SNOW_PERIOD = 200;
    public static final int HEAVY_SNOW_DUATION_START = 1800;
    public static final int HEAVY_SNOW_DUATION_END = 2000;
    public static final float HEAVY_SNOW_SCALE = 0.6f;
    public static final int HEAVY_SNOW_COUNT_MAX = 11;

    public static ShowerAnimator getShowerAnimator(RelativeLayout relativeLayout, Context context) {
        return new ShowerAnimator(relativeLayout, context, SHOWER_PERIOD, SHOWER_DURATION_START, SHOWER_DURATION_END,
                SHOWER_SCALE, SHOWER_COUNT_MAX);
    }

    public static RainAnimator getLightRainAnimator(RelativeLayout relativeLayout, Context context) {
        return new RainAnimator(relativeLayout, context, LIGHT_RAIN_PERIOD, LIGHT_RAIN_DURATION_START,
                LIGHT_RAIN_DURATION_END, LIGHT_RAIN_SCALE, LIGHT_RAIN_COUNT_MAX);
    }

    public static RainAnimator getModerateRainAnimator(RelativeLayout relativeLayout, Context context) {
        return new RainAnimator(relativeLayout, context, MODERATE_RAIN_PERIOD, MODERATE_RAIN_DURATION_START,
                MODERATE_RAIN_DURATION_END, MODERATE_RAIN_SCALE, MODERATE_RAIN_COUNT_MAX);
    }

    public static RainAnimator getHeavyRainAnimator(RelativeLayout relativeLayout, Context context) {
        return new RainAnimator(relativeLayout, context, HEAVY_RAIN_PERIOD, HEAVY_RAIN_DURATION_START,
                HEAVY_RAIN_DURATION_END, HEAVY_RAIN_SCALE, HEAVY_RAIN_COUNT_MAX);
    }
    
    public static SleetAnimator getSleetAnimator(RelativeLayout relativeLayout, Context context) {
        return new SleetAnimator(relativeLayout, context, SLEET_PERIOD, SLEET_DUATION_START, SLEET_DUATION_END,
                SLEET_SCALE, SLEET_COUNT_MAX);
    }

    public static SnowFlurryAnimator getSnowFlurryAnimator(RelativeLayout relativeLayout, Context context) {
        return new SnowFlurryAnimator(relativeLayout, context, FLURRY_PERIOD, FLURRY_DUATION_START, FLURRY_DUATION_END,
                FLURRY_SCALE, FLURRY_COUNT_MAX);
    }

    public static SnowAnimator getLightSnowAnimator(RelativeLayout relativeLayout, Context context) {
        return new SnowAnimator(relativeLayout, context, LIGHT_SNOW_PERIOD, LIGHT_SNOW_DUATION_START,
                LIGHT_SNOW_DUATION_END, LIGHT_SNOW_SCALE, LIGHT_RAIN_COUNT_MAX);
    }

    public static SnowAnimator getModerateSnowAnimator(RelativeLayout relativeLayout, Context context) {
        return new SnowAnimator(relativeLayout, context, MODERATE_SNOW_PERIOD, MODERATE_SNOW_DUATION_START,
                MODERATE_SNOW_DUATION_END, MODERATE_SNOW_SCALE, MODERATE_RAIN_COUNT_MAX);
    }

    public static SnowAnimator getHeavySnowAnimator(RelativeLayout relativeLayout, Context context) {
        return new SnowAnimator(relativeLayout, context, HEAVY_SNOW_PERIOD, HEAVY_SNOW_DUATION_START,
                HEAVY_SNOW_DUATION_END, HEAVY_SNOW_SCALE, HEAVY_RAIN_COUNT_MAX);
    }

    public static HotAirBalloonAnimator getHotAirBalloonAnimator(ImageView imageView) {
        return new HotAirBalloonAnimator(imageView);
    }
}
