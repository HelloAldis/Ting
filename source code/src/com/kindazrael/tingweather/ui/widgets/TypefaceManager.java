package com.kindazrael.tingweather.ui.widgets;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;


public class TypefaceManager {

    private final static String CUSTOMSTYLE_CRS = "crs";
    private final static String CUSTOMSTYLE_HTU = "htu";
    private final static String CUSTOMSTYLE_HTUO = "htuo";
    
    private final static String CUSTOMFONT_CRS = "Classic_round_smooth.TTF";
    private final static String CUSTOMFONT_HTU = "HelveticaNeueLTPro-UltLtEx.otf";
    private final static String CUSTOMFONT_HTUO = "HelveticaNeueLTPro-UltLtExO.otf";
    
    private static final Map <String, Typeface> typeFaceCache = new HashMap <String, Typeface>();
    
    /**
     * Get custom font from cache, load custom font from local fonts file
     * if the selected font do not exist in cache,
     * then store the loaded font into cache.
     * 
     * @param Context
     *            - Activity context
     * @param customTag
     *            - Tag for custom font
     * @param defaultFont
     *            - Default font if do not set font in XML file.
     * @return
     *         - Custom Typeface.
     */
    public static Typeface getTypeFaceByCustomTag(Context context,
            String customTag, String defaultFont) {
        Typeface tf = null;
        try {
            if (null == customTag && "".equals(customTag)) {
                customTag = defaultFont;
            }
            if (null != typeFaceCache && typeFaceCache.containsKey(customTag)) {
                tf = typeFaceCache.get(customTag);
            } else {
                String customFont = getCustomFontByCustomTag(customTag);
                tf = loadCustomTypeface(context, customFont, customTag);
            }
        } catch (final Exception e) {
            Log.e("CustomTypeface", "Could not get typeface: " + e.getMessage());
        }
        return tf;
    }
    
    /**
     * Get custom font though the custom tag which had set in XML file.
     * 
     * @param customStyle
     *            - Custom font tag.
     * @return
     *         - Custom font.
     */
    private static String getCustomFontByCustomTag(String customStyle) {
        String customFont = CUSTOMFONT_CRS;
        if(CUSTOMSTYLE_CRS.equals(customStyle)) {
            customFont = CUSTOMFONT_CRS;
        } else if(CUSTOMSTYLE_HTU.equals(customStyle)) {
            customFont = CUSTOMFONT_HTU;
        } else if (CUSTOMSTYLE_HTUO.equals(customStyle)) {
            customFont = CUSTOMFONT_HTUO;
        }
        return customFont;
    }
    
    
    /**
     * Load custom font from file.
     * 
     * @param context
     *            - Activity context
     * @param customFont
     *            - Custom font.
     * @param customTag
     *            - Custom tag
     * @return
     *         - Typeface which is load from fonts file
     */
    private static Typeface loadCustomTypeface(Context context,
            String customFont, String customTag) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(context.getAssets(), "fonts/"
                    + customFont);
            typeFaceCache.put(customTag, tf);
        } catch (final Exception e) {
            Log.e("CustomTypeface", "Could not get typeface: " + e.getMessage());
        }
        return tf;
    }
}
