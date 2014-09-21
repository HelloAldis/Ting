package com.kindazrael.tingweather.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.kindazrael.tingweather.R;


public class CustomTextView extends TextView {

    public CustomTextView (Context context) {
        super(context);
    }
    
    public CustomTextView (Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }
    
    public CustomTextView (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }
    
    private void setCustomFont(final Context ctx, final AttributeSet attrs) {
        final TypedArray a = ctx.obtainStyledAttributes(attrs,
                R.styleable.CustomTextView);
        final String customStyle = a
                .getString(R.styleable.CustomTextView_textStyle);
        // Set custom font.
        setTypeface(TypefaceManager.getTypeFaceByCustomTag(ctx, customStyle,
                null));
        a.recycle();
    }
}
